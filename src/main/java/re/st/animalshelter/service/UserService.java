package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.Stage;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;

    private final static int TEST_PERIOD_TIME = 30;

    @Autowired
    public UserService(UserRepository userRepository, AnimalService animalService, VolunteerService volunteerService) {
        this.userRepository = userRepository;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
    }

    public User getUser(long chatId) {
        return userRepository.findByChatId(chatId).orElseThrow(RuntimeException::new); //TODO
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new); //TODO
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isExist(long chatId) {
        return userRepository.findByChatId(chatId).isPresent();
    }

    @Transactional
    public ActionDTO createUserOrAction(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        boolean owner = false;
        User user;
        if (!isExist(chatId)) {
            user = new User();
            user.addStage(new Stage(Status.NONE));
            user.setChatId(chatId);
            user.setEmail(null);
            user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
            user.setPhoneNumber(null);
            user.setOwner(false);
            user.setCurrentStatus(Status.NONE);
        } else {
            user = getUser(chatId);
            owner = user.isOwner();
        }
        user.setUserName(message.chat().username());
        user.addAction(new Action(++messageId, Button.START, Shelter.NONE));
        userRepository.save(user);
        return new ActionDTO(chatId, messageId, owner, Button.START, Shelter.NONE);
    }

    public long getUserId(String userName, String email, String passport) {
        return userRepository.findUserByUserNameOrEmailOrPassport(userName, email, passport)
                .map(User::getId)
                .orElse(-1L);
    }

    public void updateData(long id, User updatedUser) {
        User user = getUserById(id);
        user.setFullName(updatedUser.getFullName());
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setPassport(updatedUser.getPassport());
        userRepository.save(user);
    }

    @Transactional
    public void attachAnimal(long userId, long animalId, long volunteerId) {
        User user = getUserById(userId);
        Animal animal = animalService.getAnimalById(animalId);
        animal.setExpirationDate(LocalDateTime.now().plusDays(TEST_PERIOD_TIME));
        animal.setReportStatus(Status.REPORT_WAS_NOT_SENT);
        Volunteer volunteer = volunteerService.getVolunteerById(volunteerId);
        volunteer.addAnimal(animal);
        user.addAnimal(animal);
        user.addVolunteer(volunteer);
        user.setOwner(true);
        volunteerService.save(volunteer);
        userRepository.save(user);
    }

    public boolean attendVolunteer(String userName) {
        Optional<User> optional = userRepository.findByUserName(userName);
        if (optional.isPresent()) {
            User user = optional.get();
            Volunteer volunteer = new Volunteer();
            volunteer.setChatId(user.getChatId());
            volunteer.setFullName(user.getFullName());
            volunteer.setUserName(user.getUserName());
            volunteerService.save(volunteer);
            return true;
        }
        return false;
    }

    @Transactional
    public Status setInitialStatus(long chatId, Button button) {
        User user = getUser(chatId);
        switch (button) {
            case LEAVE_CONTACT_INFORMATION:
                return setContactStatus(user, Status.CONTACT_INFO);
            case UNKNOWN:
                return setAnimalStatus(user, button);
        }
        return Status.NONE;
    }

    public Status setContactStatus(User user, Status nextStatus) {
        user.setCurrentStatus(nextStatus);
        user.getStage().setContactInfoStatus(nextStatus);
        saveUser(user);
        return nextStatus;
    }

    public Status setReportStatus(User user, Status currentStatus, Status nextStatus) {
        Optional<Animal> optional = user.getActiveAnimals().stream()
                .filter(animal -> animal.getReportStatus() == currentStatus)
                .findFirst();
        if (optional.isPresent()) {
            Animal animal = optional.get();
            animal.setReportStatus(nextStatus);
            user.setCurrentStatus(nextStatus);
            animalService.saveAnimal(animal);
            saveUser(user);
            return nextStatus;
        }
        return Status.NONE;
    }

    private Status setAnimalStatus(User user, Button button) {
        String callBackQuery = button.getCallBackQuery();
        long animalId;
        try {
            animalId = Long.parseLong(callBackQuery);
        } catch (Exception e) {
            return Status.NONE;
        }
        Optional<Animal> optional = user.getActiveAnimals().stream()
                .filter(animal -> animal.getId() == animalId)
                .findFirst();
        if (optional.isPresent()) {
            Animal animal = optional.get();
            user.setCurrentStatus(Status.REPORT_TEXT);
            animal.setReportStatus(Status.REPORT_TEXT);
            saveUser(user);
            animalService.saveAnimal(animal);
            return Status.REPORT_TEXT;
        }
        return Status.NONE;
    }

    @Transactional
    public Status checkTextStatus(long chatId) {
        User user = getUser(chatId);
        Status currentStatus = user.getCurrentStatus();
        switch (currentStatus) {
            case CONTACT_INFO:
                return setContactStatus(user, Status.CONTACT_INFO_RECEIVED);
            case REPORT_TEXT:
                return setReportStatus(user, currentStatus, Status.REPORT_PHOTO);
        }
        return Status.NONE;
    }

    @Transactional
    public Status checkPhotoStatus(long chatId) {
        User user = getUser(chatId);
        Status currentStatus = user.getCurrentStatus();
        switch (currentStatus) {
            case REPORT_PHOTO:
                return setReportStatus(user, currentStatus, Status.REPORTED);
        }
        return Status.NONE;
    }


//    public void createUserAndStartDialog(long chatId, int messageId, Message message) {
//        Set<Dialog> dialogs = new HashSet<>();
//        Dialog dialog = new Dialog(messageId, Shelter.NONE, Stage.START, Stage.START);
//        User user = new User();
//        dialogs.add(dialog);
//        user.setOwner(false);
//        user.setData(null);
//        user.setChatId(chatId);
//        user.setEmail(null);
//        user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
//        user.setExpirationDate(null);
//        user.setLastReportDate(null);
//        user.setPhoneNumber(null);
//        user.setPassport(null);
//        user.setUserName(message.chat().username());
//        user.setDialogs(dialogs);
//        user.setPhase(Stage.NONE);
//        userRepository.save(user);
//    }

//    public void createUserAndStartDialog(Message message) {
//        int messageId = message.messageId();
//        long chatId = message.chat().id();
//        Action action = dialogService.startNewDialog(chatId, messageId);
//        User user = userRepository.getUserByChatId(chatId);
//        if (Objects.isNull(user)) {
//            createUser(chatId, messageId, action, message);
//        }
//    }

//
//    public void createDialog(long chatId, int messageId) {
//        User user = userRepository.getUserByChatId(chatId);
//        Dialog dialog = new Dialog(messageId, Shelter.NONE, Stage.START, Stage.START);
//        user.getDialogs().add(dialog);
//        userRepository.save(user);
//    }
//
//    public void updatePhase(long chatId, Stage stage) {
//        User user = userRepository.getUserByChatId(chatId);
//        user.setPhase(stage);
//        userRepository.save(user);
//    }
//
//    public void updateReportPath(User user, String path) {
//        user.setData(path);
//        userRepository.save(user);
//    }
//
//    public void updateReportTime(User user) {
//        user.setLastReportDate(LocalDateTime.now());
//        userRepository.save(user);
//    }
//
//    public void updatePhoneNumber(long chatId, String text) {
//        User user = userRepository.getUserByChatId(chatId);
//        user.setPhoneNumber(text);
//        userRepository.save(user);
//    }
//
//    public void updateData(long id, String fullName, String phoneNumber, String email, String passport) {
//        User user = userRepository.getUserById(id);
//        if (Objects.isNull(user)) {
//            user = new User();
//        }
//        user.setFullName(fullName);
//        user.setPhoneNumber(phoneNumber);
//        user.setEmail(email);
//        user.setPassport(passport);
//        user.setExpirationDate(LocalDateTime.now().plusDays(30));
//        user.setOwner(true);
//        user.setPhase(Stage.NONE);
//        userRepository.save(user);
//    }

}

