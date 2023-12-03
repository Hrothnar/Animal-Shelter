package re.st.animalshelter.response.particular.button;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Controller;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

import java.util.LinkedList;
import java.util.Optional;

@Component
public class CallVolunteerButton implements Controller {
    private final UserService userService;
    private final StorageService storageService;
    private final TextResponse textResponse;

    @Autowired
    public CallVolunteerButton(UserService userService, StorageService storageService, TextResponse textResponse) {
        this.userService = userService;
        this.storageService = storageService;
        this.textResponse = textResponse;
    }

    @Override
    public void execute(Message message) {
        User user = userService.getByChatId(message.chat().id());
        long chatId = user.getChatId();
        LinkedList<User> volunteersAsUsers = userService.getAllByPosition(Position.VOLUNTEER);
//        Optional<User> optional = volunteersAsUsers.stream()
//                .filter(volunteerAsUser -> volunteerAsUser.getCompanionChatId() == 0)
//                .findAny();
        Optional<User> optional = Optional.of(volunteersAsUsers.get(0)); //TODO Change to the correct method
        if (optional.isPresent()) {
            User volunteerAsUser = optional.get();
            volunteerAsUser.setCompanionChatId(chatId);
            user.setCompanionChatId(volunteerAsUser.getChatId());
            volunteerAsUser.getStage().setDialogCode(Status.DIALOG.getCode());
            user.getStage().setDialogCode(Status.DIALOG.getCode());
            userService.save(volunteerAsUser);
            userService.save(user);
            String text = storageService.getByCode(Status.DIALOG_PREPARED.getCode()).getText();
            textResponse.sendNewTextResponse(chatId, text);
        }
    }
}
