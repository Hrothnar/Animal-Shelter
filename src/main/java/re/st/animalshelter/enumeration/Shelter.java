package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.Button;

public enum Shelter {
    NONE(Button.NONE.getCode(), "Любой"),
    CAT(Button.CAT_SHELTER.getCode(), "Кошки"),
    DOG(Button.DOG_SHELTER.getCode(), "Собаки");

    private final String code;
    private final String description;

    Shelter(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
