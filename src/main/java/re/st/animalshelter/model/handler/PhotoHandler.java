package re.st.animalshelter.model.handler;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.service.UserService;

@Component
public class PhotoHandler {
    private final UserService userService;
    private final TextResponse textResponse;

    @Autowired
    public PhotoHandler(UserService userService, TextResponse textResponse) {
        this.userService = userService;
        this.textResponse = textResponse;
    }

    public void processPhotoMessage(Message message) {
        long chatId = message.chat().id();
        Status status = userService.checkStatusForPhoto(message);

        textResponse.sendNewTextResponseByStatus(chatId, status);
    }
}
