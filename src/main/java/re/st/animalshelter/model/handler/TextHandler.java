package re.st.animalshelter.model.handler;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.repository.animal.AnimalRepository;
import re.st.animalshelter.service.UserService;

@Component
public class TextHandler {
    private final UserService userService;
    private final TextResponse textResponse;

    @Autowired
    public TextHandler(UserService userService, TextResponse textResponse, AnimalRepository animalRepository) {
        this.userService = userService;
        this.textResponse = textResponse;
    }

    @Transactional
    public void processTextMessage(Message message) {
        Long chatId = message.chat().id();
        if (message.text().equals(Command.START.getText())) {
            ActionDTO actionDTO = userService.createUserOrAction(message);
            textResponse.sendNewTextResponse(actionDTO);
        } else {
            Status status = userService.checkTextStatus(chatId);

            textResponse.sendNewTextResponseByStatus(chatId, status);
        }
    }

}
