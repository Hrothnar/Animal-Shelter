package re.st.animalshelter.model.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.service.InformationService;
import re.st.animalshelter.utility.ButtonUtil;

@Component
public class PhotoResponse {
    private final InformationService informationService;
    private final ButtonUtil buttonUtil;
    private final TelegramBot telegramBot;

    @Autowired
    public PhotoResponse(InformationService informationService, ButtonUtil buttonUtil, TelegramBot telegramBot) {
        this.informationService = informationService;
        this.buttonUtil = buttonUtil;
        this.telegramBot = telegramBot;
    }

    public void sendNewPhotoResponse(ActionDTO actionDTO) {
        long chatId = actionDTO.getChatId();
        String text = informationService.getText(actionDTO);
        byte[] photo = informationService.getPhoto(actionDTO);
        InlineKeyboardMarkup keyboard = buttonUtil.getKeyboard(actionDTO);
        SendPhoto response = new SendPhoto(chatId, photo).caption(text).replyMarkup(keyboard);
        BaseResponse execute = telegramBot.execute(response);
        if (!execute.isOk()) {
            System.out.println(execute.description());
        }
    }

}
