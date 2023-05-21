package re.st.animalshelter.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.response.PhotoResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.service.StorageService;

@Component
public class MapButton implements Controller {
    private final ActionService actionService;
    private final StorageService storageService;
    private final PhotoResponse photoResponse;

    @Autowired
    public MapButton(ActionService actionService, StorageService storageService, PhotoResponse photoResponse) {
        this.actionService = actionService;
        this.storageService = storageService;
        this.photoResponse = photoResponse;
    }

    @Override
    public void execute(Message message) {
        Answer answer = actionService.getCurrentAction(message, Button.MAP);
        Cell cell = storageService.getByCodeAndShelter(answer);
        byte[] photo = cell.getPhoto();
        String text = cell.getText();
        photoResponse.sendNewPhotoResponse(answer, text, photo);
    }
}
