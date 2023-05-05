package re.st.animalshelter.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.service.InformationService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.ButtonUtil;

@Component
public class TextResponse {
    private final InformationService informationService;
    private final ButtonUtil buttonUtil;
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Autowired
    public TextResponse(InformationService informationService, ButtonUtil buttonUtil, UserService userService, TelegramBot telegramBot) {
        this.informationService = informationService;
        this.buttonUtil = buttonUtil;
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    public void sendNewTextResponse(ActionDTO actionDTO) {
        long chatId = actionDTO.getChatId();
        String text = informationService.getText(actionDTO);
        InlineKeyboardMarkup keyboard = buttonUtil.getKeyboard(actionDTO);
        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
        BaseResponse execute = telegramBot.execute(response);
        if (!execute.isOk()) {
            System.out.println(execute.description());
        }
    }

    public void sendNewTextResponseByStatus(long chatId, Status status) {
        if (status != Status.NONE) {
            String text = informationService.getText(status);
            SendMessage response = new SendMessage(chatId, text);
            BaseResponse execute = telegramBot.execute(response);
            if (!execute.isOk()) {
                System.out.println(execute.description());
            }
        }
    }



}
