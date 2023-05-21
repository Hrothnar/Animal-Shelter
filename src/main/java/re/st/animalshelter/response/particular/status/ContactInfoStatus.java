package re.st.animalshelter.response.particular.status;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

@Component
public class ContactInfoStatus implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public ContactInfoStatus(UserService userService, StorageService storageService, TextResponse textResponse) {
        this.userService = userService;
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        String text = message.text();
        if (validate(text)) {
            User user = userService.getByChatId(chatId);
            user.setPhoneNumber(text);
            userService.setContactInfoStatus(user, Status.CONTACT_INFO_RECEIVED, Status.NONE);
            String storageText = storageService.getByCode(Status.CONTACT_INFO_RECEIVED.getCode()).getText();
            textResponse.sendNewTextResponse(chatId, storageText);
        }
    }

    private boolean validate(String text) {
        return text != null && text.length() > 7 && text.length() < 16;
    }
}
