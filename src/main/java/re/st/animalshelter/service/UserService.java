package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.entity.User;
import re.st.animalshelter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(long chatId) {
        return userRepository.getByChatId(chatId).isOwner();
    }

    public boolean isExist(long chatId) {
        return !Objects.isNull(userRepository.getByChatId(chatId));
    }

    public User getUser(long chatId) {
        return userRepository.getByChatId(chatId);
    }

    public void createUser(Message message) {
        long chatId = message.chat().id();
        if (!isExist(chatId)) {
            User user = new User();
            user.setOwner(false);
            user.setData(null);
            user.setChatId(chatId);
            user.setEmail(null);
            user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
            user.setExpirationDate(null);
            user.setStatus(Status.NONE);
            user.setLastReportDate(null);
            user.setPhoneNumber(null);
            userRepository.save(user);
        }
    }

    public void updatePhoneNumber(User user, String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    public void discardStatus(User user) {
        user.setStatus(Status.REPORT_WAS_NOT_SENT);
        userRepository.save(user);
    }

    public List<User> getAllActiveOwners() {
        return userRepository.findAllByOwnerTrueAndStatusNotLike(Status.NONE);
    }

    public void updateStatus(long chatId, Status status) {
        User user = userRepository.getByChatId(chatId);
        user.setStatus(status);
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
}
