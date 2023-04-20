package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.Message;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.Extension;
import re.st.animalshelter.enumeration.animal.Shelter;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flashback {
    private final Message message;
    private final Dialogue dialogue;
    private final Button button;
    private final LocalDateTime creationTime;
    private final Extension extension;
    private Shelter shelter = Shelter.NONE;

    public Flashback(Message message, Dialogue dialogue, Button button, Shelter shelter, Extension extension) {
        this.message = message;
        this.dialogue = dialogue;
        this.button = button;
        this.shelter = shelter;
        this.extension = extension;
        this.creationTime = LocalDateTime.now();
    }

    public Message getMessage() {
        return message;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public Button getButton() {
        return button;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Extension getExtension() {
        return extension;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashback flashback = (Flashback) o;
        return Objects.equals(message, flashback.message) && dialogue == flashback.dialogue && button == flashback.button && Objects.equals(creationTime, flashback.creationTime) && extension == flashback.extension && shelter == flashback.shelter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, dialogue, button, creationTime, extension, shelter);
    }

    @Override
    public String toString() {
        return "Flashback{" +
                "message=" + message +
                ", dialogue=" + dialogue +
                ", button=" + button +
                ", creationTime=" + creationTime +
                ", extension=" + extension +
                ", shelter=" + shelter +
                '}';
    }
}
