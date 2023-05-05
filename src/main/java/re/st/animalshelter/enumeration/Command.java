package re.st.animalshelter.enumeration;

public enum Command {
    START("/start");

    private final String text;

    Command(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
