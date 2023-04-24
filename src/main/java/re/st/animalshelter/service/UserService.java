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
    private final DialogRepository dialogRepository;

    @Autowired
    public UserService(UserRepository userRepository, DialogRepository dialogRepository) {
        this.userRepository = userRepository;
        this.dialogRepository = dialogRepository;
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
        user.setDialogs(dialogs);
        user.setReportPhase(Stage.NONE);
        userRepository.save(user);
    }

    public void createDialog(long chatId, int messageId) {
        User user = userRepository.getUserByChatId(chatId);
//        System.out.println("Before");
//        user.getDialogs().forEach(e -> System.out.println(e.getMessageId()));
        Dialog dialog = new Dialog(messageId, Shelter.NONE, Stage.START, Stage.START);
        user.getDialogs().add(dialog);
        userRepository.save(user);
//        System.out.println("After");
//        user.getDialogs().forEach(e -> System.out.println(e.getMessageId()));
    }

    public void updatePhase(long chatId, Stage stage) {
        User user = userRepository.getUserByChatId(chatId);
        user.setReportPhase(stage);
        userRepository.save(user);
    }



    //
//    public void updatePhoneNumber(User user, String phoneNumber) {
//        user.setPhoneNumber(phoneNumber);
//        userRepository.save(user);
//    }
//
//    public void discardStatus(User user) {
//        user.setStatus(Stage.REPORT_WAS_NOT_SENT);
//        userRepository.save(user);
//    }
//
//    public List<User> getAllActiveOwners() {
//        return userRepository.findAllByOwnerTrueAndStatusNotLike(Stage.NONE);
//    }
//
//    public void updateStatus(long chatId, Stage stage) {
//        User user = userRepository.getByChatId(chatId);
//        user.setStatus(stage);
//        userRepository.save(user);
//    }
//
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
}

