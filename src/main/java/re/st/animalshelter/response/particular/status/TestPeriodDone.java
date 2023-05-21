package re.st.animalshelter.response.particular.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.service.StorageService;

@Component
public class TestPeriodDone {
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public TestPeriodDone(StorageService storageService, TextResponse textResponse) {
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    public void execute(long chatId) {
        String text = storageService.getByCode(Status.PROBATION_SUCCESSFULLY_COMPLETED.getCode()).getText();
        textResponse.sendNewTextResponse(chatId, text);
    }
}
