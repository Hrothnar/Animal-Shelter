package re.st.animalshelter.response.particular.status;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.FileService;
import re.st.animalshelter.service.ReportService;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

import java.nio.file.Path;
import java.util.Comparator;

@Component
public class ReportPhotoStatus implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final ReportService reportService;
    private final TextResponse textResponse;
    private final FileService fileService;

    @Autowired
    public ReportPhotoStatus(UserService userService,
                             StorageService storageService,
                             ReportService reportService,
                             TextResponse textResponse,
                             FileService fileService) {
        this.userService = userService;
        this.storageService = storageService;
        this.reportService = reportService;
        this.textResponse = textResponse;
        this.fileService = fileService;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        if (validate(message.photo())) {
            ReportDTO reportDTO = userService.findReportingAnimal(user, Status.REPORT_PHOTO.getCode());
            Path path = fileService.savePhoto(user, message);
            updateReport(reportDTO, path);
            userService.setReportStatus(reportDTO, Status.REPORTED, Status.NONE);
            String text = storageService.getByCode(Status.REPORTED.getCode()).getText();
            textResponse.sendNewTextResponse(chatId, text);
        }
    }

    private void updateReport(ReportDTO reportDTO, Path path) {
        Report report = reportDTO.getUser().getReports().stream()
                .max(Comparator.comparing(Report::getId))
                .orElseThrow(RuntimeException::new); //TODO
        report.setPhotoPath(path.toString());
        reportService.saveReport(report);
    }

    private boolean validate(PhotoSize[] photoSize) {
        return photoSize != null;
    }
}
