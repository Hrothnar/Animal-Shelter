package re.st.animalshelter.model.response;

import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.model.Response;
import re.st.animalshelter.model.handler.TextHandler;

@Component
public class TextResponse implements Response {
private final TextHandler textHandler;

    public TextResponse(TextHandler textHandler) {
        this.textHandler = textHandler;
    }

    @Override
    public void execute(ActionDTO actionDTO) {

    }
}
