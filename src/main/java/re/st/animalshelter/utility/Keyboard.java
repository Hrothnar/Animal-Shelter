package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import re.st.animalshelter.dto.Answer;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Dependence;

import java.util.*;

@Service
public class Keyboard {
    public final static LinkedList<Button> START = new LinkedList<>(List.of(Button.CAT_SHELTER, Button.DOG_SHELTER));
    public final static LinkedList<Button> SHELTER = new LinkedList<>(List.of(Button.SHELTER_INFO, Button.TAKE_ANIMAL, Button.REPORT, Button.CALL_VOLUNTEER, Button.BACK));
    public final static LinkedList<Button> SHELTER_INFO = new LinkedList<>(List.of(Button.MAP, Button.DRIVER_PERMIT, Button.LEAVE_CONTACT_INFO, Button.BACK));
    public final static LinkedList<Button> TAKE_ANIMAL = new LinkedList<>(List.of(Button.RULES, Button.DOCUMENTS, Button.DISABLED_ANIMAL, Button.CYNOLOGIST, Button.BACK));
    public final static LinkedList<Button> DOCUMENTS = new LinkedList<>(List.of(Button.DRIVER_PERMIT, Button.BACK));
    public final static LinkedList<Button> BACK = new LinkedList<>(List.of(Button.BACK));

    public static InlineKeyboardMarkup collectButtons(Answer answer, LinkedList<Button> buttonSet) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Dependence person = Button.convertToDependence(answer.isOwner());
        Dependence shelter = Button.convertToDependence(answer.getShelter());
        for (Button button : buttonSet) {
            if (button.getPerson() == Dependence.ANY || button.getPerson() == person) {
                if (button.getShelter() == Dependence.ANY || button.getShelter() == shelter) {
                    keyboardMarkup.addRow(new InlineKeyboardButton(button.getDescription()).callbackData(button.getCode()));
                }
            }
        }
        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup collectButtons(User user) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        Set<Animal> animals = user.getActiveAnimals();
        for (Animal animal : animals) {
            keyboardMarkup.addRow(new InlineKeyboardButton(animal.getBreedAsString() + " -- Возраст: " + animal.getAge()).callbackData(String.valueOf(animal.getId())));
        }
        keyboardMarkup.addRow(new InlineKeyboardButton(Button.BACK.getDescription()).callbackData(Button.BACK.getCode()));
        return keyboardMarkup;
    }
}
