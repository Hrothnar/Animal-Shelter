package re.st.animalshelter.response.particular.status;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.UserService;

@Component
public class NoneStatus implements Controller {
    private final UserService userService;
    private final TextResponse textResponse;

    @Autowired
    public NoneStatus(UserService userService, TextResponse textResponse) {
        this.userService = userService;
        this.textResponse = textResponse;
    }

    @Override
    public void execute(Message message) {
        User user = userService.getByChatId(message.chat().id());
        if (user.getStage().getDialogCode().equals(Status.DIALOG.getCode())) {
            textResponse.sendTextToCompanion(user, message.text());
        }
    }
}
