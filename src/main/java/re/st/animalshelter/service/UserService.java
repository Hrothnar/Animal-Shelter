package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.enumeration.Stage;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.model.entity.Dialog;
import re.st.animalshelter.model.entity.User;
import re.st.animalshelter.repository.DialogRepository;
import re.st.animalshelter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(long chatId) {
        return userRepository.getUserByChatId(chatId).isOwner();
    }

    public boolean isExist(long chatId) {
        return !Objects.isNull(userRepository.getUserByChatId(chatId));
    }

    public User getUser(long chatId) {
        return userRepository.getUserByChatId(chatId);
    }

    public User getUser(String userName, String email, String passport) {
        return userRepository.getUserByUserNameOrEmailOrPassport(userName, email, passport);
    }

    public void createUserAndStartDialog(long chatId, int messageId, Message message) {
        Set<Dialog> dialogs = new HashSet<>();
        Dialog dialog = new Dialog(messageId, Shelter.NONE, Stage.START, Stage.START);
        User user = new User();
        dialogs.add(dialog);
        user.setOwner(false);
        user.setData(null);
        user.setChatId(chatId);
        user.setEmail(null);
        user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
        user.setExpirationDate(null);
        user.setLastReportDate(null);
        user.setPhoneNumber(null);
        user.setPassport(null);
        user.setUserName(message.chat().username());
        user.setDialogs(dialogs);
        user.setPhase(Stage.NONE);
        userRepository.save(user);
    }

    public void createDialog(long chatId, int messageId) {
        User user = userRepository.getUserByChatId(chatId);
        Dialog dialog = new Dialog(messageId, Shelter.NONE, Stage.START, Stage.START);
        user.getDialogs().add(dialog);
        userRepository.save(user);
    }

    public void updatePhase(long chatId, Stage stage) {
        User user = userRepository.getUserByChatId(chatId);
        user.setPhase(stage);
        userRepository.save(user);
    }

    public void updateReportPath(User user, String path) {
        user.setData(path);
        userRepository.save(user);
    }

    public void updateReportTime(User user) {
        user.setLastReportDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public void updatePhoneNumber(long chatId, String text) {
        User user = userRepository.getUserByChatId(chatId);
        user.setPhoneNumber(text);
        userRepository.save(user);
    }


    public void updateData(long id, String fullName, String phoneNumber, String email, String passport) {
        User user = userRepository.getUserById(id);
        if (Objects.isNull(user)) {
            user = new User();
        }
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassport(passport);
        user.setExpirationDate(LocalDateTime.now().plusDays(30));
        user.setOwner(true);
        user.setPhase(Stage.NONE);
        userRepository.save(user);
    }

}

