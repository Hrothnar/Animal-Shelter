package re.st.animalshelter.model.handler;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.dto.NotifyDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.service.UserService;

@Component
public class TextHandler {
    private final UserService userService;
    private final TextResponse textResponse;

    @Autowired
    public TextHandler(UserService userService, TextResponse textResponse) {
        this.userService = userService;
        this.textResponse = textResponse;
    }

    @Transactional
    public void processTextMessage(Message message) {
        String text = message.text();
        if (text.equals(Command.START.getText())) {
            ActionDTO actionDTO = userService.createUserOrAction(message);
            textResponse.sendNewTextResponse(actionDTO);
        } else if (text.equals(Command.FINISH.getText())) {
            finishDialog(message);
        } else {
            processFreeText(message);
        }
    }

    private void finishDialog(Message message) {
        long chatId = message.chat().id();
        NotifyDTO notifyDTO = userService.discardDialog(chatId);
        textResponse.sendNotify(notifyDTO);
    }

    private void processFreeText(Message message) {
        long chatId = message.chat().id();
        Status status = userService.handleStatusForText(message);
        if (status == Status.DIALOG) {
            User user = userService.getByChatId(chatId);
            String text = message.text();
            textResponse.sendTextToCompanion(user, text);
        } else if (status != Status.NONE) {
            textResponse.sendNewTextResponseByStatus(chatId, status);
        }
    }
}
