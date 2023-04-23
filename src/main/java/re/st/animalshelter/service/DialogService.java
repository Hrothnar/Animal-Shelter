package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.enumeration.Stage;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.model.entity.Dialog;
import re.st.animalshelter.repository.DialogRepository;

@Service
public class DialogService {
    private final DialogRepository dialogRepository;

    @Autowired
    public DialogService(DialogRepository dialogRepository) {
        this.dialogRepository = dialogRepository;
    }

    public void rebootDialog(int messageId) {
        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
        dialog.setShelter(Shelter.NONE);
        dialog.setCurrentStage(Stage.START);
        dialog.setPreviousStage(Stage.START);
        dialogRepository.save(dialog);
    }

    public void attachShelter(int messageId, Shelter shelter, Stage currentStage) {
        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
        dialog.setShelter(shelter);
        dialog.setCurrentStage(currentStage);
        dialogRepository.save(dialog);
    }

    public void forwardDialog(int messageId, Stage currentStage) {
        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
        Stage previousStage = dialog.getCurrentStage();
        dialog.setPreviousStage(previousStage);
        dialog.setCurrentStage(currentStage);
        dialogRepository.save(dialog);
    }

    public void backwardDialog(int messageId, Stage previousStage) {
        Dialog dialog = dialogRepository.getDialogByMessageId(messageId);
        Stage currentStage = dialog.getPreviousStage();
        dialog.setCurrentStage(currentStage);
        dialog.setPreviousStage(previousStage);
        dialogRepository.save(dialog);
    }

    public Dialog getDialog(int messageId) {
        return dialogRepository.getDialogByMessageId(messageId);
    }
}
