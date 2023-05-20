package re.st.animalshelter.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Position;

@Component
public class TextResponse {
    private final TelegramBot telegramBot;

    @Autowired
    public TextResponse(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendNewTextResponse(long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage response = new SendMessage(chatId, text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            throw new RuntimeException(); //TODO
        }
    }

    public void sendNewTextResponse(long chatId, String text) {
        SendMessage response = new SendMessage(chatId, text);
        if (!telegramBot.execute(response).isOk()) {
            throw new RuntimeException(); //TODO
        }
    }

    public void sendTextToCompanion(User user, String text) {
        long companionChatId = user.getCompanionChatId();
        Position position = user.getPosition();
        if (position == Position.VOLUNTEER) {
            text = "Пользователь @" + user.getUserName() + ": " + text;
        }
        SendMessage response = new SendMessage(companionChatId, text);
        if (!telegramBot.execute(response).isOk()) {
            throw new RuntimeException(); //TODO
        }
    }
}
