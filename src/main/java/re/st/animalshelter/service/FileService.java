package re.st.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.exception.IORuntimeException;
import re.st.animalshelter.utility.Distributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;

@Service
public class FileService {
    private final TelegramBot telegramBot;

    @Autowired
    public FileService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public Path saveText(User user, Message message) {
        String text = message.text();
        String preparedPath = getPreparedPath(user);
        String[] split = preparedPath.split("#");
        String reportDirectory = split[0];
        String reportFileName = split[1] + ".txt";
        Path reportsDirectoryAsPath = Path.of(reportDirectory);
        Path completePath = Path.of(reportDirectory, reportFileName);
        byte[] bytes = text.getBytes();
        try {
            Files.createDirectories(reportsDirectoryAsPath);
            Files.write(completePath, bytes);
        } catch (IOException e) {
            Distributor.LOGGER.error("IO exception");
            throw new IORuntimeException("IO exception");
        }
        return completePath;
    }

    public Path savePhoto(User user, Message message) {
        PhotoSize[] photoSizes = message.photo();
        PhotoSize photo = photoSizes[photoSizes.length - 1];
        Path completePath = null;
        String preparedPath = getPreparedPath(user).replace("#", "/");
        String fileId = photo.fileId();
        GetFile file = new GetFile(fileId);
        GetFileResponse response = telegramBot.execute(file);
        File fileFromResponse = response.file();
        if (response.isOk()) {
            try {
                byte[] bytes = telegramBot.getFileContent(fileFromResponse);
                String extension = StringUtils.getFilenameExtension(fileFromResponse.filePath());
                completePath = Path.of(preparedPath + "." + extension);
                Files.write(completePath, bytes);
            } catch (IOException e) {
                Distributor.LOGGER.error("IO exception");
                throw new IORuntimeException("IO exception");
            }
        }
        return completePath;
    }

    public String getPreparedPath(User user) {
        long id = user.getId();
        int day = LocalDateTime.now().getDayOfMonth();
        Month month = LocalDateTime.now().getMonth();
        int year = LocalDateTime.now().getYear();
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        String fullName = user.getFullName().replace(" ", "_");
        return "reports/" + fullName + "_" + id + "#" + day + "." + month + "." + year + "_" + hour + "." + minute;
    }

    public String getText(String textPath) {
        String text = "Текст не представлен";
        Path path = Path.of(textPath);
        if (path.toFile().exists()) {
            try {
                text = Files.readString(path);
            } catch (IOException e) {
                Distributor.LOGGER.error("IO exception");
                throw new IORuntimeException("IO exception");
            }
        }
        return text;
    }
}
