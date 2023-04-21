package re.st.animalshelter.model;

import com.pengrad.telegrambot.model.Message;
import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.Extension;
import re.st.animalshelter.enumeration.animal.Shelter;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flashback {
    private final Message message;
    private final LocalDateTime creationTime;
    private final Extension textExtension;
    private final Shelter shelter;
    private Dialogue dialogue;

    public Flashback(Message message, Dialogue dialogue, Shelter shelter, Extension textExtension) {
        this.message = message;
        this.dialogue = dialogue;
        this.shelter = shelter;
        this.textExtension = textExtension;
        this.creationTime = LocalDateTime.now();
    }

    public Message getMessage() {
        return message;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Extension getTextExtension() {
        return textExtension;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashback flashback = (Flashback) o;
        return Objects.equals(message, flashback.message) && Objects.equals(creationTime, flashback.creationTime) && textExtension == flashback.textExtension && shelter == flashback.shelter && dialogue == flashback.dialogue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, creationTime, textExtension, shelter, dialogue);
    }
}
