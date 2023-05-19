package re.st.animalshelter.response.particular.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.service.StorageService;

import java.text.MessageFormat;

@Component
public class AddProbationTimeStatus {
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public AddProbationTimeStatus(StorageService storageService, TextResponse textResponse) {
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    public void execute(long chatId, Animal animal, int time, Status status) {
        String type = animal instanceof Cat ? "кошки породы " : "собаки породы ";
        String days = time == 14 ? "две недели" : "один месяц";
        String text = storageService.getByCode(status.getCode()).getText();
        text = MessageFormat.format(text, type + animal.getBreedAsString().toLowerCase() + " , возрастом " + animal.getAge() + "y.", days, animal.getProbationEnd());
        textResponse.sendNewTextResponse(chatId, text);
    }
}
