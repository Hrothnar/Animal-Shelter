package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.utility.Keyboard;
import re.st.animalshelter.service.StorageService;

@Component
public class FirstButton implements Sender {
    private final EditTextResponse editTextResponse;
    private final StorageService storageService;

    @Autowired
    public FirstButton(EditTextResponse editTextResponse, StorageService storageService) {
        this.editTextResponse = editTextResponse;
        this.storageService = storageService;
    }

    @Override
    public void execute(Message message) {

    }

    @Override
    public void send(Answer answer) {
        String text = storageService.getByCodeAndOwner(answer).getText();
        InlineKeyboardMarkup keyboard = Keyboard.collectButtons(answer, Keyboard.START);
        editTextResponse.sendEditedTextResponse(answer, text, keyboard);
    }
}
