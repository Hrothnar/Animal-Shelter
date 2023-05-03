package re.st.animalshelter.model.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.PhotoResponse;
import re.st.animalshelter.service.ActionService;

import static re.st.animalshelter.model.Distributor.*;

@Component
public class CallBackQueryHandler {
    private final EditTextResponse editTextResponse;
    private final ActionService actionService;
    private final PhotoResponse photoResponse;

    @Autowired
    public CallBackQueryHandler(EditTextResponse editTextResponse, ActionService actionService, PhotoResponse photoResponse) {
        this.editTextResponse = editTextResponse;
        this.actionService = actionService;
        this.photoResponse = photoResponse;
    }

    public void processCallBackQuery(CallbackQuery callbackQuery) {
        Message message = callbackQuery.message();
        Button button = Button.getButton(callbackQuery.data());
        ActionDTO actionDTO;
        System.out.println("--------------------------------------------------------------------------");
        switch (button.getResponseType()) {
            case BACK_RESPONSE:
                actionDTO = actionService.returnLastAction(message);
                editTextResponse.sendEditedTextResponse(actionDTO);
                break;
            case EDIT_MULTIMEDIA_RESPONSE:

                break;
            case EDIT_TEXT_RESPONSE:
                actionDTO = actionService.saveAction(message, button);
                editTextResponse.sendEditedTextResponse(actionDTO);
                break;
            case PHOTO_RESPONSE:
                actionDTO = actionService.getCurrentAction(message, button);
                photoResponse.sendNewPhotoResponse(actionDTO);
                break;
            case TEXT_RESPONSE:




                break;
            default:
                throw new RuntimeException("Нет подходящего обработчика"); //TODO
        }
    }
}
