package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;

import java.util.Arrays;
import java.util.Objects;

public enum Button {
    BACK("Вернуться", "back", Stage.NONE),
    DOG_SHELTER("Приют для собак", "dog shelter", Stage.NONE),
    CAT_SHELTER("Приют для кошек", "cat shelter", Stage.NONE),
    LOOK_AT_THE_MAP("Схема проезда", "look at map", Stage.NONE),
    SHELTER_INFO("Узнать информацию о приюте", "shelter info", Stage.INFO),
    DRIVER_PERMIT("Оформить разрешение на проезд", "driver permit", Stage.DRIVER_PERMIT),
    LEAVE_CONTACT_INFORMATION("Оставить данные для связи", "leave contact information", Stage.CONTACT_INFO),
    SEND_REPORT("Отправить отчёт о питомце", "send report", Stage.REPORT_TEXT),
    TAKE_AN_ANIMAL("Как приютить питомца?", "take an animal", Stage.ANIMAL),
    RULES("Правила знакомства", "rules", Stage.RULES),
    DOCUMENTS_FOR_ANIMAL("Документы для оформления животного", "documents for animal", Stage.DOCUMENTS),
    DISABLED_ANIMAL("Животное с ограниченными возможностями", "disabled animal", Stage.DISABLED_ANIMAL),
    CYNOLOGIST("Рекомендации кинологов и контакты", "cynologist", Stage.CYNOLOGIST),
    CALL_A_VOLUNTEER("Позвать волонтёра", "call a volunteer", Stage.NONE),
    NONE("", "", Stage.NONE);

    private final String text;
    private final String calBackQuery;
    private final Stage stage;

    Button(String text, String calBackQuery, Stage stage) {
        this.text = text;
        this.calBackQuery = calBackQuery;
        this.stage = stage;
    }

    public String getText() {
        return this.text;
    }

    public String getCallBackQuery() {
        return this.calBackQuery;
    }

    public static Button getAsEnum(String string) {
        return Arrays.stream(Button.values())
                .filter(e -> Objects.equals(e.calBackQuery, string))
                .findFirst()
                .orElse(Button.NONE);
    }

    public static Shelter getShelter(Button button) {
        switch (button) {
            case CAT_SHELTER:
                return Shelter.CAT;
            case DOG_SHELTER:
                return Shelter.DOG;
        }
        return Shelter.NONE;
    }

    public Stage getStage() {
        return stage;
    }
}
