package re.st.animalshelter.enumeration.animal;

public enum Shelter {
    CAT("cat_"),
    DOG("dog_"),
    NONE("");

    private String prefix;

    Shelter(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
