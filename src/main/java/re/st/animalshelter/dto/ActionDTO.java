package re.st.animalshelter.dto;

import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;

public class ActionDTO {
    private long chatId;
    private int messageId;
    private boolean owner;
    private Button button;
    private Shelter shelter;

    public ActionDTO(long chatId, int messageId, boolean owner, Button button, Shelter shelter) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.owner = owner;
        this.button = button;
        this.shelter = shelter;
    }

    public ActionDTO(long chatId, boolean owner, Button button, Shelter shelter) {
        this.owner = owner;
        this.chatId = chatId;
        this.button = button;
        this.shelter = shelter;
    }

    public ActionDTO() {

    }

    public long getChatId() {
        return chatId;
    }

    public int getMessageId() {
        return messageId;
    }

    public Button getButton() {
        return button;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }
}
