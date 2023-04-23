package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Stage;
import re.st.animalshelter.enumeration.animal.Shelter;

import java.util.Objects;

@Component
public class AddButtonUtil {

    public InlineKeyboardMarkup getKeyboard(Shelter shelter, boolean isOwner, Stage stage) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        switch (stage) {
            case START:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CAT_SHELTER.getText()).callbackData(Button.CAT_SHELTER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DOG_SHELTER.getText()).callbackData(Button.DOG_SHELTER.getCallBackQuery()));
                break;
            case MENU:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.SHELTER_INFO.getText()).callbackData(Button.SHELTER_INFO.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.TAKE_AN_ANIMAL.getText()).callbackData(Button.TAKE_AN_ANIMAL.getCallBackQuery()));
                if (isOwner) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.SEND_REPORT.getText()).callbackData(Button.SEND_REPORT.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CALL_A_VOLUNTEER.getText()).callbackData(Button.CALL_A_VOLUNTEER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case INFO:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.LOOK_AT_THE_MAP.getText()).callbackData(Button.LOOK_AT_THE_MAP.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DRIVER_PERMIT.getText()).callbackData(Button.DRIVER_PERMIT.getCallBackQuery()));
                if (!isOwner) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.LEAVE_CONTACT_INFORMATION.getText()).callbackData(Button.LEAVE_CONTACT_INFORMATION.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case ANIMAL:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.RULES.getText()).callbackData(Button.RULES.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DOCUMENTS_FOR_ANIMAL.getText()).callbackData(Button.DOCUMENTS_FOR_ANIMAL.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DISABLED_ANIMAL.getText()).callbackData(Button.DISABLED_ANIMAL.getCallBackQuery()));
                if (Objects.equals(shelter, Shelter.DOG)) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.CYNOLOGIST.getText()).callbackData(Button.CYNOLOGIST.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CALL_A_VOLUNTEER.getText()).callbackData(Button.CALL_A_VOLUNTEER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case DOCUMENTS:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DRIVER_PERMIT.getText()).callbackData(Button.DRIVER_PERMIT.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
            case DRIVER_PERMIT:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText()).callbackData(Button.BACK.getCallBackQuery()));
                break;
        }
        return keyboardMarkup;
    }


}
