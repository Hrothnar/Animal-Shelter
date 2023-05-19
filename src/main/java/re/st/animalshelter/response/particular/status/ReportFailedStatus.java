package re.st.animalshelter.response.particular.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.service.StorageService;

@Component
public class ReportFailedStatus {
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public ReportFailedStatus(StorageService storageService, TextResponse textResponse) {
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    public void execute(long chatId) {
        String text = storageService.getByCode(Status.REPORT_FAILED.getCode()).getText();
        textResponse.sendNewTextResponse(chatId, text);
    }
}
