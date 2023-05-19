package re.st.animalshelter.model.response.particular.util;

import re.st.animalshelter.dto.Answer;

public interface Sender extends Controller {
    void send(Answer answer);
}
