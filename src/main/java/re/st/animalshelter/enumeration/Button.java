package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.model.Distributor;

import java.util.Arrays;
import java.util.Optional;

public enum Button {
    NONE("NONE", "b0", null, false, false),
    UNKNOWN("Неизвестная кнопка", "b-1", Distributor.STATUS_RESPONSE, false, false),
    BACK("Вернуться", "b1", Distributor.BACK_RESPONSE, false, false),
    START("Старт", "b2", null, false, true),
    DOG_SHELTER("Приют для собак", "b3", Distributor.EDIT_TEXT_RESPONSE, false, false),
    CAT_SHELTER("Приют для кошек", "b4", Distributor.EDIT_TEXT_RESPONSE, false, false),
    LOOK_AT_THE_MAP("Схема проезда", "b5", Distributor.PHOTO_RESPONSE, true, false),
    LEAVE_CONTACT_INFORMATION("Оставить данные для связи", "b6", Distributor.STATUS_RESPONSE, false, false),
    CALL_A_VOLUNTEER("Позвать волонтёра", "b7", Distributor.STATUS_RESPONSE, false, false),
    SHELTER_INFO("Узнать информацию о приюте", "b8", Distributor.EDIT_TEXT_RESPONSE, true, false),
    DRIVER_PERMIT("Оформить разрешение на проезд", "b9", Distributor.EDIT_TEXT_RESPONSE, false, false),
    TAKE_AN_ANIMAL("Как приютить питомца?", "b10", Distributor.EDIT_TEXT_RESPONSE, true, false),
    RULES("Правила знакомства", "b11", Distributor.EDIT_TEXT_RESPONSE, true, false),
    DOCUMENTS_FOR_ANIMAL("Документы для оформления животного", "b12", Distributor.EDIT_TEXT_RESPONSE, false, false),
    DISABLED_ANIMAL("Животное с ограниченными возможностями", "b13", Distributor.EDIT_TEXT_RESPONSE, false, false),
    CYNOLOGIST("Рекомендации кинологов и контакты", "b14", Distributor.EDIT_TEXT_RESPONSE, true, false),
    SEND_REPORT("Отправить отчёт о питомце", "b15", Distributor.EDIT_TEXT_RESPONSE, false, true);


    private String callBackQuery;
    private final String text;
    private final String responseType;
    private final boolean shelterDependence;
    private final boolean ownerDependence;

    Button(String text,
           String callBackQuery,
           String responseType,
           boolean shelterDependence,
           boolean ownerDependence) {
        this.text = text;
        this.callBackQuery = callBackQuery;
        this.shelterDependence = shelterDependence;
        this.ownerDependence = ownerDependence;
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

    public boolean isShelterDependence() {
        return shelterDependence;
    }

    public boolean isOwnerDependence() {
        return ownerDependence;
    }

    public void setCallBackQuery(String callBackQuery) {
        this.callBackQuery = callBackQuery;
    }

    public static Button getButton(String callBackQuery) {
        Optional<Button> optional = Arrays.stream(Button.values())
                .filter(button -> button.getCallBackQuery().equals(callBackQuery))
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        } else {
            Button.UNKNOWN.setCallBackQuery(callBackQuery);
            return Button.UNKNOWN;
        }
    }

    public static Shelter getShelter(Button button) {
        switch (button) {
            case CAT_SHELTER:
                return Shelter.CAT;
            case DOG_SHELTER:
                return Shelter.DOG;
        }
        throw new RuntimeException("Ошибка в выборе приюта"); //TODO
    }
}
