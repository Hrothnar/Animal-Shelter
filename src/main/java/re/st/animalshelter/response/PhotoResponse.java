package re.st.animalshelter.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;

@Component
public class PhotoResponse {
    private final TelegramBot telegramBot;

    @Autowired
    public PhotoResponse(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendNewPhotoResponse(Answer answer, String text, byte[] photo) {
        long chatId = answer.getChatId();
        SendPhoto response = new SendPhoto(chatId, photo).caption(text);
        if (!telegramBot.execute(response).isOk()) {
            throw new RuntimeException(); //TODO
        }
    }
}

