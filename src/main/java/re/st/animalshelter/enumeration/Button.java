package re.st.animalshelter.enumeration;

public enum Button {
    START("/start", "/start"),
    BACK("Вернуться", "back"),
    DOG_SHELTER("Приют для собак", "dog shelter"),
    CAT_SHELTER("Приют для кошек", "cat shelter"),
    SHELTER_INFO("Узнать информацию о приюте", "shelter info"),


    TAKE_AN_ANIMAL("Как приютить питомца?", "take an animal"),
    SEND_REPORT("Отправить отчёт о питомце", "send report"),
    CALL_A_VOLUNTEER("Позвать волонтёра", "call a volunteer"),
    WRONG("", "");

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
        for (Button button : Button.values()) {
            if (button.getCallBackQuery().equals(string)) {
                return button;
            }
        }
        return WRONG;
    }


    }
