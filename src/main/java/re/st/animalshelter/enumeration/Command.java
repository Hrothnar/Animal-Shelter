package re.st.animalshelter.enumeration;

public enum Command {
    START("/start", "начать общение c ботом"),
    FINISH("/finish", "закончить диалог с волонтёром");

    private final String text;
    private final String description;

    Command(String text, String description) {
        this.description = description;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }
}
