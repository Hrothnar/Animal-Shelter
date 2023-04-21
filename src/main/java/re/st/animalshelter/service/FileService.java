package re.st.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import re.st.animalshelter.model.entity.User;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class FileService {
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Autowired
    public FileService(UserService userService, TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    public void saveText(String text, long chatId) {
        Path reportsPath = getReportsPath(chatId);
        byte[] bytes = text.getBytes();
        try {
            Files.write(reportsPath, bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePhoto(PhotoSize[] photoSizes, long chatId) {
        Path reportsPath = getReportsPath(chatId);
        PhotoSize photo = photoSizes[photoSizes.length - 1];
        String fileId = photo.fileId();
        GetFile file = new GetFile(fileId);
        GetFileResponse response = telegramBot.execute(file);
        if (response.isOk()) {
            try {
                byte[] fileContent = telegramBot.getFileContent(response.file());
                String extension = StringUtils.getFilenameExtension(response.file().filePath());
                Files.write(Paths.get(reportsPath + "." + extension), fileContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    PhotoSize photoSize = message.photo()[message.photo().length - 1];
//                    GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
//                    if (getFileResponse.isOk()) {
//                        try {
//                            byte[] fileContent = telegramBot.getFileContent(getFileResponse.file());
//                            String extension = StringUtils.getFilenameExtension(getFileResponse.file().filePath());
//                            Files.write(Paths.get(UUID.randomUUID().toString() + "." + extension), fileContent);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }

    private Path getReportsPath(long chatId) {
        User user = userService.getUser(chatId);
        long id = user.getId();
        int day = LocalDateTime.now().getDayOfMonth();
        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        String fullName = user.getFullName().replace(" ", "_");
        return Path.of("/reports", fullName + "_" + id + "/" + day + hour + minute);
    }


}
