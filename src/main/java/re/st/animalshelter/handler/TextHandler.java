package re.st.animalshelter.handler;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.Initializer;

import java.util.Optional;

@Component
public class TextHandler {
    private final UserService userService;
    private final Initializer initializer;

    @Autowired
    public TextHandler(UserService userService, Initializer initializer) {
        this.userService = userService;
        this.initializer = initializer;
    }

    @Transactional
    public void processTextMessage(Message message) {
        long chatId = message.chat().id();
        Optional<Controller> optional = initializer.getCommandByValue(message.text());
        if (optional.isPresent()) {
            optional.get().execute(message);
        } else {
            User user = userService.getByChatId(chatId);
            initializer.getStatus(user.getCurrentCode()).execute(message);
        }
    }
}
