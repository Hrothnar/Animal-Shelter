package re.st.animalshelter.enumeration;

import re.st.animalshelter.exception.ShelterNotFoundException;
import re.st.animalshelter.utility.Distributor;

import java.util.Collections;
import java.util.LinkedList;

public enum Button {
    UNKNOWN(Dependence.ANY, Dependence.ANY, "b-1", "Неизвестная кнопка"),
    NONE(Dependence.ANY, Dependence.ANY, "b0", "666"),
    BACK(Dependence.ANY, Dependence.ANY, "b1", "Вернуться"),

    DOG_SHELTER(Dependence.ANY, Dependence.ANY, "b2", "Приют для собак"),
    CAT_SHELTER(Dependence.ANY, Dependence.ANY, "b3", "Приют для кошек"),

    MAP(Dependence.ANY, Dependence.ANY, "b4", "Схема проезда"),
    CALL_VOLUNTEER(Dependence.ANY, Dependence.ANY, "b5", "Позвать волонтёра"),
    SHELTER_INFO(Dependence.ANY, Dependence.ANY, "b6", "Узнать информацию о приюте"),
    DRIVER_PERMIT(Dependence.ANY, Dependence.ANY, "b7", "Оформить разрешение на проезд"),
    TAKE_ANIMAL(Dependence.ANY, Dependence.ANY, "b8", "Как приютить питомца?"),
    RULES(Dependence.ANY, Dependence.ANY, "b9", "Правила знакомства"),
    DOCUMENTS(Dependence.ANY, Dependence.ANY, "b10", "Документы для оформления животного"),
    DISABLED_ANIMAL(Dependence.ANY, Dependence.ANY, "b11", "Животное с ограниченными возможностями"),
    CYNOLOGIST(Dependence.ANY, Dependence.DOG, "b12", "Рекомендации кинологов и контакты"),
    LEAVE_CONTACT_INFO(Dependence.USER, Dependence.ANY, "b13", "Оставить данные для связи"),
    REPORT(Dependence.OWNER, Dependence.ANY, "b14", "Отправить отчёт о питомце");


    private final String code;
    private final String description;
    private final Dependence person;
    private final Dependence shelter;

    Button(Dependence person, Dependence shelter, String code, String description) {
        this.person = person;
        this.shelter = shelter;
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCode() {
        return this.code;
    }

    public Dependence getPerson() {
        return person;
    }

    public Dependence getShelter() {
        return shelter;
    }

    public static Dependence convertToDependence(boolean owner) {
        return owner ? Dependence.OWNER : Dependence.USER;
    }

    public static Dependence convertToDependence(Shelter shelter) {
        switch (shelter) {
            case NONE: return Dependence.ANY;
            case CAT: return Dependence.CAT;
            case DOG: return Dependence.DOG;
        }
        Distributor.LOGGER.error("No such shelter");
        throw new ShelterNotFoundException("No such shelter");
    }

    public static LinkedList<Button> getValidButtons() {
        LinkedList<Button> buttons = new LinkedList<>();
        Collections.addAll(buttons, DOG_SHELTER, CAT_SHELTER, MAP, CALL_VOLUNTEER, SHELTER_INFO, DRIVER_PERMIT, TAKE_ANIMAL);
        Collections.addAll(buttons, RULES, DOCUMENTS, DISABLED_ANIMAL, CYNOLOGIST, LEAVE_CONTACT_INFO, REPORT);
        return buttons;
    }
}
