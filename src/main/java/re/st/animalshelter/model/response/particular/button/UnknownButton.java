package re.st.animalshelter.model.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.dto.ReportDTO;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.model.response.particular.util.Controller;
import re.st.animalshelter.model.response.particular.util.Sender;
import re.st.animalshelter.service.AnimalService;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

import java.util.Optional;

@Component
public class UnknownButton implements Controller {
    private final UserService userService;
    private final TextResponse textResponse;
    private final StorageService storageService;
    private String callBackQuery;

    public UnknownButton(UserService userService, TextResponse textResponse, StorageService storageService) {
        this.userService = userService;
        this.textResponse = textResponse;
        this.storageService = storageService;
    }

    public UnknownButton setCallBackQuery(String callBackQuery) {
        this.callBackQuery = callBackQuery;
        return this;
    }

    @Override
    public void execute(Message message) {
        long chatId = message.chat().id();
        ReportDTO reportDTO = findReportingAnimal(message);
        userService.setReportStatus(reportDTO, Status.REPORT_TEXT, Status.REPORT_TEXT);
        String text = storageService.getByCode(Status.REPORT_TEXT.getCode()).getText();
        textResponse.sendNewTextResponse(chatId, text);
    }

    private ReportDTO findReportingAnimal(Message message) {
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        long animalId;
        try {
            animalId = Long.parseLong(callBackQuery);
        } catch (Exception e) {
            throw new RuntimeException(); //TODO
        }
        Animal animal = user.getActiveAnimals().stream()
                .filter(a -> a.getId() == animalId)
                .findFirst().orElseThrow(RuntimeException::new);//TODO
        return new ReportDTO(user, animal);
    }
}
