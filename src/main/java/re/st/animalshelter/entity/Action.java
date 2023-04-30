package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "dialogs")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @Column(name = "message_id", nullable = false)
    private int messageId;
    @Enumerated(value = EnumType.STRING)
    private Button button;
    @Enumerated(value = EnumType.STRING)
    private Shelter shelter;
    @Column(name = "lats_update", nullable = false)
    private LocalDateTime lastUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}