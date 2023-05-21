package re.st.animalshelter.response.particular.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.Stage;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.Shelter;
import re.st.animalshelter.response.TextResponse;
import re.st.animalshelter.response.particular.connect.Sender;
import re.st.animalshelter.utility.Keyboard;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;

@Component
public class StartCommand implements Sender {
    private final UserService userService;
    private final TextResponse textResponse;
    private final StorageService storageService;

    @Autowired
    public StartCommand(UserService userService, TextResponse textResponse, StorageService storageService) {
        this.userService = userService;
        this.textResponse = textResponse;
        this.storageService = storageService;
    }

    @Override
    public void execute(Message message) {
        Answer answer = createUserOrAction(message);
        send(answer);
    }

    @Override
    public void send(Answer answer) {
        String text = storageService.getByCodeAndOwner(answer).getText();
        InlineKeyboardMarkup keyboard = Keyboard.collectButtons(answer, Keyboard.START);
        textResponse.sendNewTextResponse(answer.getChatId(), text, keyboard);
    }

    private Answer createUserOrAction(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        boolean owner = false;
        User user;
        if (!userService.isExist(chatId)) {
            user = new User();
            user.addStage(new Stage(Status.NONE.getCode(), Status.NONE.getCode()));
            user.setChatId(chatId);
            user.setEmail(null);
            user.setFullName(message.chat().firstName() + " " + message.chat().lastName());
            user.setPhoneNumber(null);
            user.setOwner(false);
            user.setPosition(Position.USER);
            user.setCompanionChatId(0);
            user.setCurrentCode(Status.NONE.getCode());
        } else {
            user = userService.getByChatId(chatId);
            owner = user.isOwner();
        }
        user.setUserName(message.chat().username());
        user.addAction(new Action(++messageId, Command.START.getCode(), Shelter.NONE));
        userService.save(user);
        return new Answer(chatId, messageId, owner, Command.START.getCode(), Shelter.NONE);
    }
}
