package re.st.animalshelter.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.response.particular.button.UnknownButton;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.utility.Initializer;

@Component
public class CallBackQueryHandler {
    private final ActionService actionService;
    private final Initializer initializer;
    private final UnknownButton unknownButton;

    @Autowired
    public CallBackQueryHandler(ActionService actionService, Initializer initializer, UnknownButton unknownButton) {
        this.actionService = actionService;
        this.initializer = initializer;
        this.unknownButton = unknownButton;
    }

    @Transactional
    public void processCallBackQuery(CallbackQuery callbackQuery) {
        Message message = callbackQuery.message();
        String queryData = callbackQuery.data();
        if (queryData.equals(Button.BACK.getCode())) {
            Answer answer = actionService.returnLastAction(message);
            initializer.getSender(answer.getCode()).get().send(answer);
        } else {
            initializer.getController(queryData).orElseGet(() -> unknownButton.setCallBackQuery(queryData)).execute(message);
        }
    }
}
