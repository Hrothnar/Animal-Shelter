package re.st.animalshelter.enumeration;

import java.util.Arrays;
import java.util.Objects;

public enum Button {
    START("/start", "/start"),
    BACK("Вернуться", "back"),
    DOG_SHELTER("Приют для собак", "dog shelter"),
    CAT_SHELTER("Приют для кошек", "cat shelter"),
    SHELTER_INFO("Узнать информацию о приюте", "shelter info"),
    LOOK_AT_THE_MAP("Схема проезда", "look at map"),
    DRIVER_PERMIT("Оформить разрешение на проезд", "driver permit"),
    LEAVE_CONTACT_INFORMATION("Оставить данные для связи", "leave contact information"),


    TAKE_AN_ANIMAL("Как приютить питомца?", "take an animal"),
    SEND_REPORT("Отправить отчёт о питомце", "send report"),
    CALL_A_VOLUNTEER("Позвать волонтёра", "call a volunteer"),
    NONE("", "");

    private final String text;
    private final String calBackQuery;

    Button(String text, String calBackQuery) {
        this.text = text;
        this.calBackQuery = calBackQuery;
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
}
