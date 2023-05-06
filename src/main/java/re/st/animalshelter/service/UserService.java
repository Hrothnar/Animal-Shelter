package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.*;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;
    private final FileService fileService;

    private final static int TEST_PERIOD_TIME = 30;

    @Autowired
    public UserService(UserRepository userRepository,
                       AnimalService animalService,
                       VolunteerService volunteerService,
                       FileService fileService) {
        this.userRepository = userRepository;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
        this.fileService = fileService;
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
            user.addStage(new Stage(Status.NONE, Status.NONE));
            user.setChatId(chatId);
            user.setEmail(null);
            user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
            user.setPhoneNumber(null);
            user.setOwner(false);
            user.setCompanionChatId(0);
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
        volunteerService.saveVolunteer(volunteer);
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
            volunteerService.saveVolunteer(volunteer);
            return true;
        }
        return false;
    }

    @Transactional
    public Status setInitialStatus(long chatId, Button button) {
        User user = getUser(chatId);
        switch (button) {
            case LEAVE_CONTACT_INFORMATION:
                return setContactStatus(user, Status.CONTACT_INFO, true);
            case CALL_A_VOLUNTEER:
                return setVolunteer(user);
            case UNKNOWN:
                return setInitialAnimalStatus(user, button);
        }
        return Status.NONE;
    }

    private Status setVolunteer(User user) {
        long userChatId = user.getChatId();
        LinkedList<Volunteer> volunteers = volunteerService.getAllVolunteers();
//        int i = new Random().nextInt(volunteers.size()) + 1;
        Volunteer volunteer = volunteers.get(0);
        long volunteerChatId = volunteer.getChatId();
        User volunteerAsUser = getUser(volunteerChatId);
        volunteerAsUser.setCompanionChatId(userChatId);
        user.setCompanionChatId(volunteerChatId);
        volunteerAsUser.getStage().setDialogStatus(Status.DIALOG);
        user.getStage().setDialogStatus(Status.DIALOG);
        saveUser(volunteerAsUser);
        saveUser(user);
        return Status.PREPARE_FOR_DIALOG;
    }

    private Status setContactStatus(User user, Status nextStatus, boolean correct) {
        if (correct) {
            user.setCurrentStatus(nextStatus);
            user.getStage().setContactInfoStatus(nextStatus);
            saveUser(user);
            return nextStatus;
        }
        return Status.NONE;
    }

    private boolean savePhoneNumber(User user, String text, boolean correct) {
        if (correct) {
            user.setPhoneNumber(text);
            return true;
        }
        return false;
    }

    private Status setReportStatus(ReportDTO reportDTO, Status nextStatus, boolean correct) {
        if (correct) {
            User user = reportDTO.getUser();
            Animal animal = reportDTO.getAnimal();
            animal.setReportStatus(nextStatus);
            user.setCurrentStatus(nextStatus);
            animalService.saveAnimal(animal);
            saveUser(user);
            return nextStatus;
        }
        return Status.NONE;
    }


    private Status setInitialAnimalStatus(User user, Button button) {
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

    private ReportDTO getReportDTO(User user, Status currentStatus) {
        Optional<Animal> optional = user.getActiveAnimals().stream()
                .filter(animal -> animal.getReportStatus() == currentStatus)
                .findFirst();
        if (optional.isPresent()) {
            Animal animal = optional.get();
            return new ReportDTO(user, animal);
        }
        return null;
    }

    @Transactional
    public Status checkStatusForText(Message message) {
        long chatId = message.chat().id();
        String text = message.text();
        User user = getUser(chatId);
        Status currentStatus = user.getCurrentStatus();
        boolean correct;
        switch (currentStatus) {
            case CONTACT_INFO:
                correct = fileService.validateTextData(text, currentStatus);
                correct = savePhoneNumber(user, text, correct);
                return setContactStatus(user, Status.CONTACT_INFO_RECEIVED, correct);
            case REPORT_TEXT:
                ReportDTO reportDTO = getReportDTO(user, currentStatus);
                correct = fileService.validateTextData(text, currentStatus);
                fileService.saveText(user, text, correct);
                return setReportStatus(reportDTO, Status.REPORT_PHOTO, correct);
            default:
                Status stageStatus = user.getStage().getDialogStatus();
                if (stageStatus == Status.DIALOG) {
                    return stageStatus;
                }
        }
        return Status.NONE;
    }

    @Transactional
    public Status checkStatusForPhoto(Message message) {
        long chatId = message.chat().id();
        PhotoSize[] photoSizes = message.photo();
        User user = getUser(chatId);
        Status currentStatus = user.getCurrentStatus();
        boolean correct;
        switch (currentStatus) {
            case REPORT_PHOTO:
                ReportDTO reportDTO = getReportDTO(user, currentStatus);
                fileService.savePhoto(user, photoSizes, true);
                //TODO Validator?
                correct = saveReport(reportDTO);
                return setReportStatus(reportDTO, Status.REPORTED, correct);
        }
        return Status.NONE;
    }

    private boolean saveReport(ReportDTO reportDTO) {
        if (Objects.nonNull(reportDTO)) {
            User user = reportDTO.getUser();
            Animal animal = reportDTO.getAnimal();
            String path = fileService.getReportDirectory(user);
            user.addReport(new Report(path, user, animal));
            saveUser(user);
            return true;
        }
        return false;
    }

    public void discardDialog(Long chatId) {
        User finisher = getUser(chatId);
        Optional<User> optional = userRepository.findByCompanionChatId(chatId);
        if (optional.isPresent()) {
            User companion = optional.get();
            companion.setCompanionChatId(0);
            companion.setCurrentStatus(Status.NONE);
            companion.getStage().setDialogStatus(Status.NONE);
            saveUser(companion);
        }
        finisher.setCurrentStatus(Status.NONE);
        finisher.setCompanionChatId(0);
        finisher.getStage().setDialogStatus(Status.NONE);
        saveUser(finisher);
    }
}

