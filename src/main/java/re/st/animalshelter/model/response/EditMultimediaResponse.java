package re.st.animalshelter.model.response;

import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.model.Response;
import re.st.animalshelter.model.handler.CallBackQueryHandler;

@Component
public class EditMultimediaResponse implements Response {
    private final CallBackQueryHandler callBackQueryHandler;

    public EditMultimediaResponse(CallBackQueryHandler callBackQueryHandler) {
        this.callBackQueryHandler = callBackQueryHandler;
    }


    @Override
    public void execute(ActionDTO actionDTO) {

    }
}
