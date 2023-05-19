package re.st.animalshelter.enumeration;

import java.util.Collections;
import java.util.LinkedList;

public enum Command {
    START("c0", "/start", "начать общение c ботом"),
    FINISH("c1", "/finish", "закончить диалог с волонтёром");

    private final String code;
    private final String value;
    private final String description;

    Command(String code, String value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static LinkedList<Command> getValidCommands() {
        LinkedList<Command> commands = new LinkedList<>();
        Collections.addAll(commands, START);
        return commands;
    }
}
