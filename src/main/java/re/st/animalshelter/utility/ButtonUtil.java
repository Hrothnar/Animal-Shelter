package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Animal;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.service.UserService;

import java.util.Set;

@Component
public class ButtonUtil {
    private final UserService userService;

    @Autowired
    public ButtonUtil(UserService userService) {
        this.userService = userService;
    }

    public InlineKeyboardMarkup getKeyboard(ActionDTO actionDTO) {
        Shelter shelter = actionDTO.getShelter();
        Button button = actionDTO.getButton();
        boolean owner = actionDTO.isOwner();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        switch (button) {
            case START:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CAT_SHELTER.getText()).callbackData(Button.CAT_SHELTER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DOG_SHELTER.getText()).callbackData(Button.DOG_SHELTER.getCallBackQuery()));
                break;
            case CAT_SHELTER:
            case DOG_SHELTER:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.SHELTER_INFO.getText()).callbackData(Button.SHELTER_INFO.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.TAKE_AN_ANIMAL.getText()).callbackData(Button.TAKE_AN_ANIMAL.getCallBackQuery()));
                if (owner) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.SEND_REPORT.getText()).callbackData(Button.SEND_REPORT.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CALL_A_VOLUNTEER.getText()).callbackData(Button.CALL_A_VOLUNTEER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case SHELTER_INFO:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.LOOK_AT_THE_MAP.getText()).callbackData(Button.LOOK_AT_THE_MAP.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DRIVER_PERMIT.getText()).callbackData(Button.DRIVER_PERMIT.getCallBackQuery()));
                if (!owner) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.LEAVE_CONTACT_INFORMATION.getText()).callbackData(Button.LEAVE_CONTACT_INFORMATION.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case TAKE_AN_ANIMAL:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.RULES.getText()).callbackData(Button.RULES.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DOCUMENTS_FOR_ANIMAL.getText()).callbackData(Button.DOCUMENTS_FOR_ANIMAL.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DISABLED_ANIMAL.getText()).callbackData(Button.DISABLED_ANIMAL.getCallBackQuery()));
                if (shelter.equals(Shelter.DOG)) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.CYNOLOGIST.getText()).callbackData(Button.CYNOLOGIST.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CALL_A_VOLUNTEER.getText()).callbackData(Button.CALL_A_VOLUNTEER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case DOCUMENTS_FOR_ANIMAL:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DRIVER_PERMIT.getText()).callbackData(Button.DRIVER_PERMIT.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case CYNOLOGIST:
            case DISABLED_ANIMAL:
            case RULES:
            case DRIVER_PERMIT:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
            case SEND_REPORT:
                keyboardMarkup = dynamicGenerator(actionDTO.getChatId());
                break;
            default:
                if ()
        }
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup dynamicGenerator(long chatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Set<Animal> animals = userService.getUser(chatId).getAnimals();
        if (animals.size() > 1) {
            for (Animal animal : animals) {
                keyboardMarkup.addRow(new InlineKeyboardButton(animal.getBreed() + " " + animal.getAge()).callbackData(String.valueOf(animal.getId())));
            }
        }
        return keyboardMarkup;
    }


}
