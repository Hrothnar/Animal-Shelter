package re.st.animalshelter.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.exception.FileGettingException;
import re.st.animalshelter.exception.MessageSendingException;
import re.st.animalshelter.utility.Distributor;

import java.io.IOException;

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
            Distributor.LOGGER.error("Message was not sent");
            throw new MessageSendingException("Message was not sent, check input data");
        }
    }

    public void sendPhotoToCompanion(User user, Message message) {
        byte[] bytes = getPhoto(message);
        String text = message.caption() != null ? message.caption() : "";
        long companionChatId = user.getCompanionChatId();
        Position position = user.getPosition();
        if (position == Position.VOLUNTEER) {
            text = "Пользователь @" + user.getUserName() + ": " + text;
        }
        SendPhoto response = new SendPhoto(companionChatId, bytes).caption(text);
        if (!telegramBot.execute(response).isOk()) {
            Distributor.LOGGER.error("Message was not sent");
            throw new MessageSendingException("Message was not sent, check input data");
        }
    }

    private byte[] getPhoto(Message message) {
        PhotoSize[] photo = message.photo();
        PhotoSize photoSize = photo[photo.length - 1];
        GetFile getFile = new GetFile(photoSize.fileId());
        File file = telegramBot.execute(getFile).file();
        byte[] bytes;
        try {
            bytes = telegramBot.getFileContent(file);
        } catch (IOException e) {
            Distributor.LOGGER.error("Failed to get photo");
            throw new FileGettingException("Failed to get photo");
        }
        return bytes;
    }
}

