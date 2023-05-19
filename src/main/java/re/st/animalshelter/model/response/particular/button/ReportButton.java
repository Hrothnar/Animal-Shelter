package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.utility.Keyboard;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

@Component
public class ReportButton implements Sender {
    private final ActionService actionService;
    private final UserService userService;
    private final StorageService storageService;
    private final EditTextResponse editTextResponse;

    @Autowired
    public ReportButton(ActionService actionService,
                        UserService userService,
                        StorageService storageService,
                        EditTextResponse editTextResponse) {
        this.actionService = actionService;
        this.userService = userService;
        this.storageService = storageService;
        this.editTextResponse = editTextResponse;
    }

    @Override
    public void execute(Message message) {
        Answer answer = actionService.rememberAction(message, Button.REPORT);
        send(answer);
    }

    @Override
    public void send(Answer answer) {
        User user = userService.getByChatId(answer.getChatId());
        InlineKeyboardMarkup keyboard = Keyboard.collectButtons(user);
        String text = storageService.getByCodeAndOwner(answer).getText();
        editTextResponse.sendEditedTextResponse(answer, text, keyboard);
    }
}
