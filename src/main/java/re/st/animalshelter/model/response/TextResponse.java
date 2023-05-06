package re.st.animalshelter.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.VolunteerService;
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

    public void sendNewTextResponseByStatus(User user, String textForCompanion, Status status) {
        String text;
        long chatId;
        if (status != Status.NONE) {
            if (status != Status.DIALOG) {
                chatId = user.getChatId();
                text = storageService.getText(status);
            } else {
                chatId = user.getCompanionChatId();
                text = textForCompanion;
            }
            SendMessage response = new SendMessage(chatId, text);
            SendResponse execute = telegramBot.execute(response);
            if (!execute.isOk()) {
                System.out.println(execute.description());
            }
        }
    }
}
