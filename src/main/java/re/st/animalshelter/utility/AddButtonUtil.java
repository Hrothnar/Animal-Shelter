package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.animal.Shelter;

import static re.st.animalshelter.listener.TelegramBotListener.LOGGER;

@Component
public class AddButtonUtil {

    public InlineKeyboardMarkup getKeyboard(Dialogue dialogue, boolean isOwner, Shelter shelter) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        switch (dialogue) {
            case START:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CAT_SHELTER.getText())
                        .callbackData(Button.CAT_SHELTER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.DOG_SHELTER.getText())
                        .callbackData(Button.DOG_SHELTER.getCallBackQuery()));
                break;
            case MENU:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.SHELTER_INFO.getText())
                        .callbackData(Button.SHELTER_INFO.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.TAKE_AN_ANIMAL.getText())
                        .callbackData(Button.TAKE_AN_ANIMAL.getCallBackQuery()));
                if (isOwner) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(Button.SEND_REPORT.getText())
                            .callbackData(Button.SEND_REPORT.getCallBackQuery()));
                }
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.CALL_A_VOLUNTEER.getText())
                        .callbackData(Button.CALL_A_VOLUNTEER.getCallBackQuery()));
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText())
                        .callbackData(Button.BACK.getCallBackQuery()));
                break;
            case INFO:
                keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getText())
                        .callbackData(Button.BACK.getCallBackQuery()));
                break;
            default:
                LOGGER.error("Неподдерживаемая кнопка");
                //TODO доработать
        }
        return keyboardMarkup;
    }


}
