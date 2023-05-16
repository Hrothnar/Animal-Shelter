package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.repository.ActionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActionService {
    private final ActionRepository actionRepository;
    private final UserService userService;

    @Autowired
    public ActionService(ActionRepository actionRepository, UserService userService) {
        this.actionRepository = actionRepository;
        this.userService = userService;
    }

    public Action getLastAction(int messageId) {
        return actionRepository.findLastActionByMessageId(messageId).orElseThrow(RuntimeException::new); //TODO
    }

    public ActionDTO getCurrentAction(Message message, Button button) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        Action lastAction = getLastAction(messageId);
        Shelter shelter = lastAction.getShelter();
        boolean owner = userService.getByChatId(chatId).isOwner();
        return new ActionDTO(chatId, messageId, owner, button, shelter);
    }

    @Transactional
    public ActionDTO saveAction(Message message, Button button) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        Shelter shelter;
        Action lastAction = getLastAction(messageId);
        Action currentAction = new Action();
        if (lastAction.getButton().equals(Button.START)) {
            shelter = Button.getShelter(button);
        } else {
            shelter = lastAction.getShelter();
        }
        currentAction.setShelter(shelter);
        currentAction.setMessageId(messageId);
        currentAction.setButton(button);
        User user = userService.getByChatId(chatId);
        boolean owner = user.isOwner();
        user.addAction(currentAction);
        userService.saveUser(user);
        return new ActionDTO(chatId, messageId, owner, button, shelter);
    }

    @Transactional
    public ActionDTO returnLastAction(Message message) {
        int messageId = message.messageId();
        long chatId = message.chat().id();
        User user = userService.getByChatId(chatId);
        LinkedList<Action> actions = user.getActions().stream()
                .filter(action -> action.getMessageId() == messageId)
                .distinct()
                .sorted(Comparator.comparing(Action::getId).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
        Action currentAction = actions.poll();
        user.removeAction(currentAction);
        Action previousAction = actions.peek();
        Button button = previousAction.getButton();
        boolean owner = user.isOwner();
        Shelter shelter = previousAction.getShelter();
        userService.saveUser(user);
        return new ActionDTO(chatId, messageId, owner, button, shelter);
    }
}
