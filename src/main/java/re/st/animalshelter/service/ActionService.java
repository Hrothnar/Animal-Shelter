package re.st.animalshelter.service;

import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.Answer;
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

    @Transactional
    public Answer rememberShelter(Message message, Shelter shelter) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        User user = userService.getByChatId(chatId);
        String code = shelter.getCode();
        Action action = new Action(messageId, code, shelter);
        user.addAction(action);
        userService.save(user);
        return new Answer(chatId, messageId, user.isOwner(), code, shelter);
    }

    @Transactional
    public Answer rememberAction(Message message, Button button) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        User user = userService.getByChatId(chatId);
        String code = button.getCode();
        Action lastAction = getLastAction(messageId);
        Shelter shelter = lastAction.getShelter();
        Action action = new Action(messageId, code, shelter);
        user.addAction(action);
        userService.save(user);
        return new Answer(chatId, messageId, user.isOwner(), code, shelter);
    }

    @Transactional
    public Answer returnLastAction(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        User user = userService.getByChatId(chatId);
        LinkedList<Action> actions = user.getActions().stream()
                .filter(action -> action.getMessageId() == messageId)
                .distinct()
                .sorted(Comparator.comparing(Action::getId).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
        Action currentAction = actions.poll();
        Action previousAction = actions.peek();
        user.removeAction(currentAction);
        userService.save(user);
        return new Answer(chatId, messageId, user.isOwner(), previousAction.getCode(), previousAction.getShelter());
    }

    public Answer getCurrentAction(Message message, Button button) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        Action lastAction = getLastAction(messageId);
        Shelter shelter = lastAction.getShelter();
        boolean owner = userService.getByChatId(chatId).isOwner();
        return new Answer(chatId, messageId, owner, button.getCode(), shelter);
    }
}
