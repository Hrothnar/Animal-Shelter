package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.particular.status.AddProbationTimeStatus;
import re.st.animalshelter.response.particular.status.EndProbationStatus;
import re.st.animalshelter.repository.UserRepository;
import re.st.animalshelter.utility.Distributor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final VolunteerService volunteerService;
    private final AddProbationTimeStatus addProbationTimeStatus;
    private final EndProbationStatus endProbationStatus;

    private final static int TEST_PERIOD_TIME = 30;

    @Autowired
    public UserService(UserRepository userRepository,
                       AnimalService animalService,
                       VolunteerService volunteerService,
                       AddProbationTimeStatus addProbationTimeStatus, EndProbationStatus endProbationStatus) {
        this.userRepository = userRepository;
        this.animalService = animalService;
        this.volunteerService = volunteerService;
        this.addProbationTimeStatus = addProbationTimeStatus;
        this.endProbationStatus = endProbationStatus;
    }

    public User getByChatId(long chatId) {
        return userRepository.findByChatId(chatId).orElseThrow(RuntimeException::new); //TODO
    }

    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new); //TODO
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean isExist(long chatId) {
        return userRepository.findByChatId(chatId).isPresent();
    }

    public User getByCompanionChatId(Long chatId) {
        return userRepository.findByCompanionChatId(chatId).orElseThrow(RuntimeException::new);//TODO
    }

    public long getId(long id, String userName) {
        return userRepository.findUserByIdOrUserName(id, userName).map(User::getId).orElse(-1L);
    }

    public LinkedList<User> getAll() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getUserName))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public LinkedList<User> getAllByPosition(Position position) {
        return userRepository.findAllByPosition(position).stream()
                .sorted(Comparator.comparing(User::getUserName))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void updateData(long id, User updatedUser) {
        User user = getById(id);
        user.setFullName(updatedUser.getFullName());
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setPassport(updatedUser.getPassport());
        userRepository.save(user);
    }

    @Transactional
    public void attachAnimal(long userId, long animalId, long volunteerId) {
        User user = getById(userId);
        Animal animal = animalService.getById(animalId);
        animal.setExpirationDate(LocalDateTime.now().plusDays(TEST_PERIOD_TIME));
        animal.setReportCode(Status.NONE.getCode());
        Volunteer volunteer = volunteerService.getById(volunteerId);
        volunteer.addAnimal(animal);
        user.addAnimal(animal);
        user.setOwner(true);
        volunteerService.save(volunteer);
        save(user);
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
            volunteerService.save(volunteer);
            save(user);
            return true;
        }
        return false;
    }

    public void removeVolunteer(long volunteerId) {
        Volunteer volunteer = volunteerService.getById(volunteerId);
        User volunteerAsUser = getByChatId(volunteer.getChatId());
        volunteerAsUser.setPosition(Position.USER);
        volunteer.getAnimals().forEach(animal -> animal.setVolunteer(null));
        volunteerService.remove(volunteer);
        save(volunteerAsUser);
    }

    public ReportDTO findReportingAnimal(User user, String code) {
        Optional<Animal> optional = user.getActiveAnimals().stream()
                .filter(animal -> animal.getReportCode().equals(code))
                .findFirst();
        if (optional.isPresent()) {
            return new ReportDTO(user, optional.get());
        }
        throw new RuntimeException();//TODO
    }

    public void setReportStatus(ReportDTO reportDTO, Status reportStatus, Status userStatus) {
        User user = reportDTO.getUser();
        Animal animal = reportDTO.getAnimal();
        animal.setReportCode(reportStatus.getCode());
        user.setCurrentCode(userStatus.getCode());
        animalService.save(animal);
        save(user);
    }

    public void setContactInfoStatus(User user, Status contactStatus, Status userStatus) {
        user.getStage().setContactInfoCode(contactStatus.getCode());
        user.setCurrentCode(userStatus.getCode());
        save(user);
    }

    public void updateProbation(long userId, long animalId, String button) {
        User user = getById(userId);
        Animal animal = animalService.getById(animalId);
        if (button.equals(Distributor.PASS)) {
            animal.setActive(false);
            endProbation(user, animal);
            endProbationStatus.execute(user.getChatId(), animal, Status.PROBATION_SUCCESSFULLY_COMPLETED);
        } else {
            user.removeAnimal(animal);
            endProbation(user, animal);
            endProbationStatus.execute(user.getChatId(), animal, Status.PROBATION_NOT_COMPLETED);
        }
    }

    private void endProbation(User user, Animal animal) {
        Volunteer volunteer = animal.getVolunteer();
        animal.setExpirationDate(null);
        animal.setReportCode(Status.NONE.getCode());
        volunteer.removeAnimal(animal);
        animalService.save(animal);
        volunteerService.save(volunteer);
        if (user.getActiveAnimals().size() == 0) {
            user.setOwner(false);
        }
        save(user);
    }

    public void addProbationTime(long userId, long animalId, int time) {
        Animal animal = animalService.getById(animalId);
        User user = getById(userId);
        animal.setExpirationDate(animal.getExpirationDate().plusDays(time));
        addProbationTimeStatus.execute(user.getChatId(), animal, time, Status.ADD_TIME);
    }
}

