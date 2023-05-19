package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.utility.Keyboard;
import re.st.animalshelter.service.StorageService;

@Component
public class DogShelterButton implements Sender {
    private final ActionService actionService;
    private final EditTextResponse editTextResponse;
    private final StorageService storageService;

    @Autowired
    public DogShelterButton(ActionService actionService, EditTextResponse editTextResponse, StorageService storageService) {
        this.actionService = actionService;
        this.editTextResponse = editTextResponse;
        this.storageService = storageService;
    }

    @Override
    public void execute(Message message) {
        Answer answer = actionService.rememberShelter(message, Shelter.DOG);
        send(answer);
    }

    @Override
    public void send(Answer answer) {
        String text = storageService.getByCode(answer.getCode()).getText();
        InlineKeyboardMarkup keyboard = Keyboard.collectButtons(answer, Keyboard.SHELTER);
        editTextResponse.sendEditedTextResponse(answer, text, keyboard);
    }
}
