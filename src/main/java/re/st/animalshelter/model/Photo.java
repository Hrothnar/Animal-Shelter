package re.st.animalshelter.model;

import re.st.animalshelter.enumeration.Dialogue;
import re.st.animalshelter.enumeration.Extension;
import re.st.animalshelter.enumeration.animal.Shelter;

public class Photo {
    private final long chatId;
    private final Dialogue dialogue;
    private final Shelter shelter;
    private final Extension photoExtension;
    private final Extension textExtension;

    public Photo(long chatId, Dialogue dialogue, Shelter shelter, Extension photoExtension, Extension textExtension) {
        this.chatId = chatId;
        this.dialogue = dialogue;
        this.shelter = shelter;
        this.photoExtension = photoExtension;
        this.textExtension = textExtension;
    }

    public long getChatId() {
        return chatId;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Extension getPhotoExtension() {
        return photoExtension;
    }

    public Extension getTextExtension() {
        return textExtension;
    }
}
