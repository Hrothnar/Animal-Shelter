package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.repository.ActionRepository;

@Service
public class ActionService {
    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }
//
//    public long getDialogId(int messageId) {
//        return dialogRepository.getDialogByMessageId(messageId).getId();
//    }
//
//    public void attachShelter(int messageId, Button button, Stage stage) {
//        Shelter shelter = Button.getShelter(button);
//        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
//        dialog.setShelter(shelter);
//        dialog.setCurrentStage(stage);
//        dialogRepository.save(dialog);
//    }
//
//    public void resetDialog(int messageId) {
//        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
//        dialog.setShelter(Shelter.NONE);
//        dialog.setCurrentStage(Stage.START);
//        dialog.setPreviousStage(Stage.START);
//        dialogRepository.save(dialog);
//    }
//
//    public void forwardDialog(int messageId, Stage currentStage) {
//        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
//        Stage previousStage = dialog.getCurrentStage();
//        dialog.setPreviousStage(previousStage);
//        dialog.setCurrentStage(currentStage);
//        dialogRepository.save(dialog);
//    }
//
//    public void backwardDialog(int messageId, Stage previousStage) {
//        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
//        Stage currentStage = dialog.getPreviousStage();
//        dialog.setCurrentStage(currentStage);
//        dialog.setPreviousStage(previousStage);
//        dialogRepository.save(dialog);
//    }



//
//    public Action startNewDialog(long chatId, int messageId) {
//        Set<Action> actions = new HashSet<>();
//        Action action = new Action();
//        action.setChatId(chatId);
//        action.setMessageId(++messageId);
//        action.setButton(Button.START);
//        action.setLastUpdate(LocalDateTime.now());
//        dialogRepository.save(action);
//        return action;
//    }
//
//    public Action saveDialog(long chatId, int messageId, Button button) {
//        Action lastAction = dialogRepository.getTopByMessageId(messageId);
//        Action action = new Action();
//        if (Button.START.equals(lastAction.getButton())) {
//            action.setShelter(Button.getShelter(button));
//        } else {
//            action.setShelter(lastAction.getShelter());
//        }
//        action.setChatId(chatId);
//        action.setMessageId(messageId);
//        action.setButton(button);
//        action.setLastUpdate(LocalDateTime.now());
//        dialogRepository.save(action);
//        return action;
//    }

    public Action getLastDialog(int messageId) {
        return actionRepository.getTopByMessageId(messageId);
    }

}
