package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.model.Distributor;

import java.util.Arrays;
import java.util.Objects;

public enum Button {
    START("/start", "/start", null, false, true),

    BACK("Вернуться", "back", Distributor.BACK_RESPONSE, false, false),

    DOG_SHELTER("Приют для собак", "dog shelter", Distributor.EDIT_TEXT_RESPONSE, false, false),
    CAT_SHELTER("Приют для кошек", "cat shelter", Distributor.EDIT_TEXT_RESPONSE, false, false),

    SHELTER_INFO("Узнать информацию о приюте", "shelter info", Distributor.EDIT_TEXT_RESPONSE, true, false),
    DRIVER_PERMIT("Оформить разрешение на проезд", "driver permit", Distributor.EDIT_TEXT_RESPONSE, false, false),
    TAKE_AN_ANIMAL("Как приютить питомца?", "take an animal", Distributor.EDIT_TEXT_RESPONSE, true, false),
    RULES("Правила знакомства", "rules", Distributor.EDIT_TEXT_RESPONSE, true, false),
    DOCUMENTS_FOR_ANIMAL("Документы для оформления животного", "documents for animal", Distributor.EDIT_TEXT_RESPONSE, false, false),
    DISABLED_ANIMAL("Животное с ограниченными возможностями", "disabled animal", Distributor.EDIT_TEXT_RESPONSE, false, false),
    CYNOLOGIST("Рекомендации кинологов и контакты", "cynologist", Distributor.EDIT_TEXT_RESPONSE, true, false),

    LOOK_AT_THE_MAP("Схема проезда", "look at map", Distributor.PHOTO_RESPONSE, true, false),

    SEND_REPORT("Отправить отчёт о питомце", "send report", Distributor.TEXT_RESPONSE, false, false),
    LEAVE_CONTACT_INFORMATION("Оставить данные для связи", "leave contact information", Distributor.TEXT_RESPONSE, false, false),
    CALL_A_VOLUNTEER("Позвать волонтёра", "call a volunteer", Distributor.TEXT_RESPONSE, false, false);

    private final String text;
    private final String callBackQuery;
    private final String responseType;
    private final boolean responseDependsOnShelter;
    private final boolean responseDependsOnOwner;

    Button(String text, String callBackQuery, String responseType, boolean responseDependsOnShelter, boolean responseDependsOnOwner) {
        this.text = text;
        this.callBackQuery = callBackQuery;
        this.responseDependsOnShelter = responseDependsOnShelter;
        this.responseDependsOnOwner = responseDependsOnOwner;
        this.responseType = responseType;
    }

    public String getText() {
        return this.text;
    }

    public String getCallBackQuery() {
        return this.callBackQuery;
    }

    public String getResponseType() {
        return responseType;
    }

    public boolean isResponseDependsOnShelter() {
        return responseDependsOnShelter;
    }

    public boolean isResponseDependsOnOwner() {
        return responseDependsOnOwner;
    }

    public static Button getButton(String string) {
        return Arrays.stream(Button.values())
                .filter(e -> Objects.equals(e.callBackQuery, string) || Objects.equals(e.toString(), string))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public static Shelter getShelter(Button button) {
        switch (button) {
            case CAT_SHELTER:
                return Shelter.CAT;
            case DOG_SHELTER:
                return Shelter.DOG;
        }
        throw new RuntimeException("Ошибка в выборе приюта");
    }
}
