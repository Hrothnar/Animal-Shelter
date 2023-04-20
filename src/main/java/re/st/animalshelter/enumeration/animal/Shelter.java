package re.st.animalshelter.enumeration.animal;

public enum Shelter {
    CAT("cat/", "cat_"),
    DOG("dog/", "dog_"),
    NONE("", "");

    private String path;
    private String prefix;

    Shelter(String path, String prefix) {
        this.path = path;
        this.prefix = prefix;
    }

    public String getPath() {
        return path;
    }

    public String getPrefix() {
        return prefix;
    }
}
