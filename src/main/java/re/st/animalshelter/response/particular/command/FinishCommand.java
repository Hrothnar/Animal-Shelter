package re.st.animalshelter.response.particular.command;

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
public class FinishCommand implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public FinishCommand(UserService userService, StorageService storageService, TextResponse textResponse) {
        this.userService = userService;
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        long companionChatId = user.getCompanionChatId();
        String text = storageService.getByCode(Status.DIALOG_FINISHED.getCode()).getText();
        if (user.getStage().getDialogCode().equals(Status.DIALOG.getCode()) && companionChatId != 0) {
            User companion = userService.getByCompanionChatId(companionChatId);
            user.setCompanionChatId(0);
            user.getStage().setDialogCode(Status.NONE.getCode());
            userService.save(user);
            textResponse.sendNewTextResponse(chatId, text);
            if (companion.getStage().getDialogCode().equals(Status.DIALOG.getCode()) && companion.getCompanionChatId() != 0) {
                companion.setCompanionChatId(0);
                companion.getStage().setDialogCode(Status.NONE.getCode());
                userService.save(companion);
                textResponse.sendNewTextResponse(companionChatId, text);
            }
        }
    }
}
