package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.dto.NotifyDTO;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.*;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.model.Validator;
import re.st.animalshelter.repository.UserRepository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;
    private final FileService fileService;
    private final ReportService reportService;
    private final Validator validator;

    private final static int TEST_PERIOD_TIME = 30;

    @Autowired
    public UserService(UserRepository userRepository,
                       AnimalService animalService,
                       VolunteerService volunteerService,
                       FileService fileService,
                       Validator validator,
                       ReportService reportService) {
        this.userRepository = userRepository;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
        this.fileService = fileService;
        this.validator = validator;
        this.reportService = reportService;
    }

    public User getByChatId(long chatId) {
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

    public User findByCompanionChatId(Long chatId) {
        return userRepository.findByCompanionChatId(chatId).orElseThrow(RuntimeException::new);//TODO
    }

    public long getUserId(long id, String userName) {
        return userRepository.findUserByIdOrUserName(id, userName)
                .map(User::getId)
                .orElse(-1L);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getUserName))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<User> getAllUsersByPosition(Position position) {
        return userRepository.findAllByPosition(position).stream()
                .sorted(Comparator.comparing(User::getUserName))
                .collect(Collectors.toCollection(LinkedList::new));
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
            user.setPosition(Position.USER);
            user.setCompanionChatId(0);
            user.setCurrentStatus(Status.NONE);
        } else {
            user = getByChatId(chatId);
            owner = user.isOwner();
        }
        user.setUserName(message.chat().username());
        user.addAction(new Action(++messageId, Button.START, Shelter.NONE));
        userRepository.save(user);
        return new ActionDTO(chatId, messageId, owner, Button.START, Shelter.NONE);
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

    public boolean attendVolunteer(long userId, String userName) {
        Optional<User> optional = userRepository.findUserByIdOrUserName(userId, userName);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setPosition(Position.VOLUNTEER);
            Volunteer volunteer = new Volunteer();
            volunteer.setChatId(user.getChatId());
            volunteer.setFullName(user.getFullName());
            volunteer.setUserName(user.getUserName());
            volunteerService.saveVolunteer(volunteer);
            saveUser(user);
            return true;
        }
        return false;
    }

    @Transactional
    public Status setInitialStatus(long chatId, Button button) {
        User user = getByChatId(chatId);
        switch (button) {
            case LEAVE_CONTACT_INFORMATION:
                return setContactStatus(user, Status.CONTACT_INFO, Status.CONTACT_INFO);
            case CALL_A_VOLUNTEER:
                return relateUserAndVolunteer(user);
            case UNKNOWN:
                return setInitialAnimalStatus(user, button);
        }
        return Status.NONE;
    }

    private Status relateUserAndVolunteer(User user) {
        long userChatId = user.getChatId();
        LinkedList<User> volunteersAsUsers = userRepository.findAllByPosition(Position.VOLUNTEER); //TODO
//        Optional<User> optional = volunteersAsUsers.stream()
//                .filter(volunteerAsUser -> volunteerAsUser.getCompanionChatId() == 0)
//                .findAny();
//        if (optional.isPresent()) {
        User volunteerAsUser = volunteersAsUsers.get(0);
//            User volunteerAsUser = optional.get();
        volunteerAsUser.setCompanionChatId(userChatId);
        user.setCompanionChatId(volunteerAsUser.getChatId());
        volunteerAsUser.getStage().setDialogStatus(Status.DIALOG);
        user.getStage().setDialogStatus(Status.DIALOG);
        saveUser(volunteerAsUser);
        saveUser(user);
        return Status.PREPARED_FOR_DIALOG;
//        }
//        return Status.NONE;
    }

    private Status setContactStatus(User user, Status contactStatus, Status userStatus) {
        user.setCurrentStatus(userStatus);
        user.getStage().setContactInfoStatus(contactStatus);
        saveUser(user);
        return contactStatus;
    }

    private Status setReportStatus(ReportDTO reportDTO, Status reportStatus, Status userStatus) {
        User user = reportDTO.getUser();
        Animal animal = reportDTO.getAnimal();
        animal.setReportStatus(reportStatus);
        user.setCurrentStatus(userStatus);
        animalService.saveAnimal(animal);
        saveUser(user);
        return reportStatus;
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

    @Transactional
    public Status handleStatusForText(Message message) {
        String text = message.text();
        User user = getByChatId(message.chat().id());
        Status currentStatus = user.getCurrentStatus();
        switch (currentStatus) {
            case CONTACT_INFO:
                if (validator.isTextCorrect(text, currentStatus)) {
                    user.setPhoneNumber(text);
                    return setContactStatus(user, Status.CONTACT_INFO_RECEIVED, Status.NONE);
                }
                break;
            case REPORT_TEXT:
                if (validator.isTextCorrect(text, currentStatus)) {
                    ReportDTO reportDTO = findReportingAnimal(user, currentStatus);
                    Path path = fileService.saveText(user, message);
                    createOrRecreateReport(reportDTO, path);
                    return setReportStatus(reportDTO, Status.REPORT_PHOTO, Status.REPORT_PHOTO);
                }
                break;
            case NONE:
                if (user.getStage().getDialogStatus() == Status.DIALOG) {
                    return Status.DIALOG;
                }
                break;
        }
        return Status.NONE;
    }

    @Transactional
    public Status handleStatusForPhoto(Message message) {
        User user = getByChatId(message.chat().id());
        Status currentStatus = user.getCurrentStatus();
        switch (currentStatus) {
            case REPORT_PHOTO:
                if (validator.isPhotoCorrect(message.photo(), currentStatus)) {
                    ReportDTO reportDTO = findReportingAnimal(user, currentStatus);
                    Path path = fileService.savePhoto(user, message);
                    updateReport(reportDTO, path);
                    return setReportStatus(reportDTO, Status.REPORTED, Status.NONE);
                }
                break;
        }
        return Status.NONE;
    }

    private ReportDTO findReportingAnimal(User user, Status currentStatus) {
        Optional<Animal> optional = user.getActiveAnimals().stream()
                .filter(animal -> animal.getReportStatus() == currentStatus)
                .findFirst();
        if (optional.isPresent()) {
            return new ReportDTO(user, optional.get());
        }
        throw new RuntimeException();//TODO
    }

    private void createOrRecreateReport(ReportDTO reportDTO, Path path) {
        User user = reportDTO.getUser();
        Animal animal = reportDTO.getAnimal();
        Report report = user.getReports().stream()
                .filter(r -> r.getAnimal().getId() == animal.getId())
                .filter(r -> r.getTime().getDayOfYear() == LocalDateTime.now().getDayOfYear())
                .max(Comparator.comparing(Report::getId))
                .orElseGet(Report::new);
        report.setStatus(Status.NONE);
        report.setAnimal(animal);
        report.setTextPath(path.toString());
        user.addReport(report);
        saveUser(user);
    }

    private void updateReport(ReportDTO reportDTO, Path path) {
        Report report = reportDTO.getUser().getReports().stream()
                .max(Comparator.comparing(Report::getId))
                .orElseThrow(RuntimeException::new); //TODO
        report.setPhotoPath(path.toString());
        reportService.saveReport(report);
    }

    public NotifyDTO discardDialog(long chatId) {
        User user = getByChatId(chatId);
        long companionChatId = user.getCompanionChatId();
        if (user.getStage().getDialogStatus() == Status.DIALOG && companionChatId != 0) {
            User companion = findByCompanionChatId(companionChatId);
            user.setCompanionChatId(0);
            user.getStage().setDialogStatus(Status.NONE);
            companion.setCompanionChatId(0);
            companion.getStage().setDialogStatus(Status.NONE);
            saveUser(user);
            saveUser(companion);
            return new NotifyDTO(chatId, companionChatId, Status.DIALOG_FINISHED);
        }
        return new NotifyDTO(chatId, Status.NONE);
    }
}

