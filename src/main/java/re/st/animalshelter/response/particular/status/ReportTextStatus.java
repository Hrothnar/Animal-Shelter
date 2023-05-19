package re.st.animalshelter.response.particular.status;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.FileService;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Comparator;

@Component
public class ReportTextStatus implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final TextResponse textResponse;
    private final FileService fileService;

    @Autowired
    public ReportTextStatus(UserService userService,
                            StorageService storageService,
                            TextResponse textResponse,
                            FileService fileService) {
        this.userService = userService;
        this.storageService = storageService;
        this.textResponse = textResponse;
        this.fileService = fileService;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        if (validate(message.text())) {
            ReportDTO reportDTO = userService.findReportingAnimal(user, Status.REPORT_TEXT.getCode());
            Path path = fileService.saveText(user, message);
            createOrRecreateReport(reportDTO, path);
            userService.setReportStatus(reportDTO, Status.REPORT_PHOTO, Status.REPORT_PHOTO);
            String storageText = storageService.getByCode(Status.REPORT_PHOTO.getCode()).getText();
            textResponse.sendNewTextResponse(chatId, storageText);
        }
    }

    private void createOrRecreateReport(ReportDTO reportDTO, Path path) {
        User user = reportDTO.getUser();
        Animal animal = reportDTO.getAnimal();
        Report report = user.getReports().stream()
                .filter(r -> r.getAnimal().getId() == animal.getId())
                .filter(r -> r.getTime().getDayOfYear() == LocalDateTime.now().getDayOfYear())
                .max(Comparator.comparing(Report::getId))
                .orElseGet(Report::new);
        report.setReportCode(Status.NONE.getCode());
        report.setAnimal(animal);
        report.setTextPath(path.toString());
        user.addReport(report);
        userService.save(user);
    }

    private boolean validate(String text) {
        return  text != null && text.length() > 35;
    }
}
