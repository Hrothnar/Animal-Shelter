package re.st.animalshelter.handler;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.Initializer;

@Component
public class PhotoHandler {
    private final UserService userService;
    private final Initializer initializer;

    @Autowired
    public PhotoHandler(UserService userService, Initializer initializer) {
        this.userService = userService;
        this.initializer = initializer;
    }

    @Transactional
    public void processPhotoMessage(Message message) {
        User user = userService.getByChatId(message.chat().id());
        initializer.getStatus(user.getCurrentCode()).execute(message);
    }
}
