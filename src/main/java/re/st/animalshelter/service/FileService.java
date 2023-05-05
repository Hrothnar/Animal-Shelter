package re.st.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import re.st.animalshelter.entity.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;

@Service
public class FileService {
    private final UserService userService;
    private final TelegramBot telegramBot;

    @Autowired
    public FileService(UserService userService, TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }
//
//    public void saveText(long chatId, String text) {
//        String reportsFullPath = getReportsPath(chatId);
//        String[] split = reportsFullPath.split("#");
//        String reportsDirectory = split[0];
//        String reportsFile = split[1] + ".txt";
//        Path reportsDirectoryPath = Path.of(reportsDirectory);
//        Path reportsFilePath = Path.of(reportsDirectory, reportsFile);
//        byte[] bytes = text.getBytes();
//        try {
//            Files.createDirectories(reportsDirectoryPath);
//            Files.write(reportsFilePath, bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void savePhoto(long chatId, PhotoSize[] photoSizes) {
//        String fullPath = getReportsPath(chatId).replace("#", "/");
//        PhotoSize photo = photoSizes[photoSizes.length - 1];
//        String fileId = photo.fileId();
//        GetFile file = new GetFile(fileId);
//        GetFileResponse response = telegramBot.execute(file);
//        File fileFromResponse = response.file();
//        if (response.isOk()) {
//            try {
//                byte[] fileContent = telegramBot.getFileContent(fileFromResponse);
//                String extension = StringUtils.getFilenameExtension(fileFromResponse.filePath());
//                Path reportsFilePath = Path.of(fullPath + "." + extension);
//                Files.write(reportsFilePath, fileContent);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
////    public String getReportsPath(long chatId) {
////        User user = userService.getUser(chatId);
////        long id = user.getId();
////        int day = LocalDateTime.now().getDayOfMonth();
////        Month month = LocalDateTime.now().getMonth();
////        int year = LocalDateTime.now().getYear();
////        int hour = LocalDateTime.now().getHour();
////        int minute = LocalDateTime.now().getMinute();
////        String fullName = user.getFullName().replace(" ", "_");
////        userService.updateReportPath(user, "reports/" + fullName + "_" + id);
////        return "reports/" + fullName + "_" + id + "#" + day + "." + month + "." + year + "_" + hour + "." + minute;
////    }
}
