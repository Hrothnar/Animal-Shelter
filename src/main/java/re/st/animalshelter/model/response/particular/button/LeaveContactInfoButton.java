package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.model.response.particular.util.Controller;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

@Component
public class LeaveContactInfoButton implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public LeaveContactInfoButton(UserService userService,
                                  StorageService storageService,
                                  TextResponse textResponse) {
        this.userService = userService;
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        userService.setContactInfoStatus(user, Status.CONTACT_INFO, Status.CONTACT_INFO);
        String text = storageService.getByCode(Button.LEAVE_CONTACT_INFO.getCode()).getText();
        textResponse.sendNewTextResponse(chatId, text);
    }
}
