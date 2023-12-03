package re.st.animalshelter.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.exception.MessageSendingException;
import re.st.animalshelter.utility.Distributor;

@Component
public class EditTextResponse {
    private final TelegramBot telegramBot;

    @Autowired
    public EditTextResponse(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendEditedTextResponse(Answer answer, String text, InlineKeyboardMarkup keyboard) {
        long chatId = answer.getChatId();
        int messageId = answer.getMessageId();
        EditMessageText response = new EditMessageText(chatId, messageId, text).replyMarkup(keyboard);
        if (!telegramBot.execute(response).isOk()) {
            Distributor.LOGGER.error("Message was not sent");
            throw new MessageSendingException("Message was not sent, check input data");
        }
    }
}
