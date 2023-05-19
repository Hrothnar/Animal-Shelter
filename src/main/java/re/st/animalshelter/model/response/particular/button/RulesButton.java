package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.utility.Keyboard;
import re.st.animalshelter.service.StorageService;

@Component
public class RulesButton implements Sender {
    private final ActionService actionService;
    private final StorageService storageService;
    private final EditTextResponse editTextResponse;

    @Autowired
    public RulesButton(ActionService actionService, StorageService storageService, EditTextResponse editTextResponse) {
        this.actionService = actionService;
        this.storageService = storageService;
        this.editTextResponse = editTextResponse;
    }

    @Override
    public void execute(Message message) {
        Answer answer = actionService.rememberAction(message, Button.RULES);
        send(answer);
    }

    @Override
    public void send(Answer answer) {
        String text = storageService.getByCodeAndShelter(answer).getText();
        InlineKeyboardMarkup keyboard = Keyboard.collectButtons(answer, Keyboard.BACK);
        editTextResponse.sendEditedTextResponse(answer, text, keyboard);
    }
}
