package re.st.animalshelter.model;

import com.pengrad.telegrambot.model.PhotoSize;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Status;

@Component
public class Validator {

    public boolean isTextCorrect(String text, Status currentStatus) {
        switch (currentStatus) {
            case CONTACT_INFO:
                if (text.length() > 7 && text.length() < 16) {
                    return true;
                }
            case REPORT_TEXT:
                if (text.length() > 35) {
                    return true;
                }
        }
        return false;
    }

    public boolean isPhotoCorrect(PhotoSize[] photoSizes, Status currentStatus) {
        if (photoSizes != null) {
            return true;
        }
        return false;
    }
}
