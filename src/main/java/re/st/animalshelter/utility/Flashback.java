package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.Message;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.animal.Shelter;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flashback {
    private final Message message;
    private final Dialogue dialogue;
    private final Button button;
    private Shelter shelter = Shelter.NONE;
    private final LocalDateTime creationTime;

    public Flashback(Message message, Dialogue dialogue, Button button, Shelter shelter) {
        this.message = message;
        this.dialogue = dialogue;
        this.button = button;
        this.shelter = shelter;
        this.creationTime = LocalDateTime.now();
    }

    public Message getMessage() {
        return message;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Button getButton() {
        return button;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashback flashback = (Flashback) o;
        return Objects.equals(message, flashback.message) && dialogue == flashback.dialogue && button == flashback.button && shelter == flashback.shelter && Objects.equals(creationTime, flashback.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, dialogue, button, shelter, creationTime);
    }

    @Override
    public String toString() {
        return "Flashback{" +
//                "message=" + message +
                ", dialogue=" + dialogue +
                ", button=" + button +
                ", shelter=" + shelter +
//                ", creationTime=" + creationTime +
                '}';
    }
}
