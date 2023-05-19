package re.st.animalshelter.model.response.particular.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.service.StorageService;

import java.text.MessageFormat;

@Component
public class EndProbationStatus {
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public EndProbationStatus(StorageService storageService, TextResponse textResponse) {
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    public void execute(long chatId, Animal animal, Status status) {
        String type = animal instanceof Cat ? "кошки породы " : "собаки породы ";
        String text = storageService.getByCode(status.getCode()).getText();
        text = MessageFormat.format(text,type + animal.getBreedAsString().toLowerCase() + " , возрастом " + animal.getAge() + "y.");
        textResponse.sendNewTextResponse(chatId, text);
    }
}
