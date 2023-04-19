package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.model.entity.User;
import re.st.animalshelter.repository.UserRepository;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(long chatId) {
        return userRepository.getByChatId(chatId).isActive();
    }

    public boolean isExist(long chatId) {
        return !Objects.isNull(userRepository.getByChatId(chatId));
    }

    public void createUser(Message message) {
        User user = new User();
        user.setActive(false);
        user.setData(null);
        user.setChatId(message.chat().id());
        user.setEmail(null);
        user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
        user.setExpirationDate(null);
        user.setStage(null);
        user.setLastReportDate(null);
        user.setPhoneNumber(null);
        userRepository.save(user);
    }


}
