package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.shelter.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int messageId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Button button;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Shelter shelter;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Action(int messageId, Button button, Shelter shelter) {
        this.messageId = messageId;
        this.button = button;
        this.shelter = shelter;
        this.lastUpdate = LocalDateTime.now();
    }

    public Action() {

    }

    public long getId() {
        return id;
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
        this.lastUpdate = LocalDateTime.now();
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}