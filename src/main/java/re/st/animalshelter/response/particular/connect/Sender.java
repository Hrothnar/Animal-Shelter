package re.st.animalshelter.response.particular.connect;

import re.st.animalshelter.dto.Answer;

public interface Sender extends Controller {
    void send(Answer answer);
}
