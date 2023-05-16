package re.st.animalshelter.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.dto.NotifyDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.utility.ButtonUtil;

@Component
public class TextResponse {
    private final StorageService storageService;
    private final ButtonUtil buttonUtil;
    private final TelegramBot telegramBot;

    @Autowired
    public TextResponse(StorageService storageService, ButtonUtil buttonUtil, TelegramBot telegramBot) {
        this.storageService = storageService;
        this.buttonUtil = buttonUtil;
        this.telegramBot = telegramBot;
    }

    public void sendNewTextResponse(ActionDTO actionDTO) {
        long chatId = actionDTO.getChatId();
        String text = storageService.getText(actionDTO);
        InlineKeyboardMarkup keyboard = buttonUtil.getKeyboard(actionDTO);
        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
        BaseResponse execute = telegramBot.execute(response);
        if (!execute.isOk()) {
            System.out.println(execute.description());
        }
    }

    public void sendNewTextResponseByStatus(long chatId, Status status) {
        if (status != Status.NONE) {
            String text = storageService.getText(status);
            SendMessage response = new SendMessage(chatId, text);
            BaseResponse execute = telegramBot.execute(response);
            if (!execute.isOk()) {
                System.out.println(execute.description());
            }
        }
    }

    public void sendTextToCompanion(User user, String text) {
        long companionChatId = user.getCompanionChatId();
        Position position = user.getPosition();
        if (position == Position.VOLUNTEER) {
            text = "Пользователь @" + user.getUserName() + ": " + text;
        }
        SendMessage response = new SendMessage(companionChatId, text);
        SendResponse execute = telegramBot.execute(response);
        if (!execute.isOk()) {
            System.out.println(execute.description());
        }
    }

    public void sendNotify(NotifyDTO notifyDTO) {
        Status status = notifyDTO.getStatus();
        switch (status) {
            case DIALOG_FINISHED:
                sendNewTextResponseByStatus(notifyDTO.getUserChatId(), status);
                sendNewTextResponseByStatus(notifyDTO.getCompanionChatId(), status);
                break;
            case FAILED:
                sendNewTextResponseByStatus(notifyDTO.getUserChatId(), status);
        }
    }
}
