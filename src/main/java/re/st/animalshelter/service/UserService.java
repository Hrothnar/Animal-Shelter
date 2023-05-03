package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.Stage;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long chatId) {
        return userRepository.findByChatId(chatId).orElseThrow(RuntimeException::new); //TODO
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
            user.setOwner(false);
            user.setUserName(message.chat().username());
            user.setChatId(chatId);
            user.setEmail(null);
            user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
            user.setPhoneNumber(null);
        } else {
            user = getUser(chatId);
            owner = user.isOwner();
        }
        user.addAction(new Action(++messageId, Button.START, Shelter.NONE));
        userRepository.save(user);
        return new ActionDTO(chatId, messageId, owner, Button.START, Shelter.NONE);
    }




















//    public User getUser(String userName, String email, String passport) {
//        return userRepository.findUserByUserNameOrEmailOrPassport(userName, email, passport);
//    }


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

