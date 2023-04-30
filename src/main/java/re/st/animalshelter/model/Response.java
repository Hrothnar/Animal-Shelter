package re.st.animalshelter.model;

import re.st.animalshelter.dto.ActionDTO;

public interface Response {
    void execute(ActionDTO actionDTO);
}
