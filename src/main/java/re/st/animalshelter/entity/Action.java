package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "message_id", nullable = false)
    private int messageId;
    @Column(name = "code", nullable = false, length = 4)
    private String code;
    @Column(name = "shelter", nullable = false, length = 16)
    @Enumerated(value = EnumType.STRING)
    private Shelter shelter;
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Action(int messageId, String code, Shelter shelter) {
        this.messageId = messageId;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String button) {
        this.code = button;
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