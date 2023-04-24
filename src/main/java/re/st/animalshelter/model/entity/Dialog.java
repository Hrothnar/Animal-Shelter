package re.st.animalshelter.model.entity;

import re.st.animalshelter.enumeration.Stage;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "dialogs")
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "message_id", nullable = false, unique = true)
    private int messageId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Shelter shelter;
    @Column(name = "current_stage", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Stage currentStage;
    @Column(name =  "previous_stage",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Stage previousStage;
    @Column(name = "lats_update", nullable = false)
    private LocalDateTime lastUpdate;

    public Dialog(int messageId, Shelter shelter, Stage currentStage, Stage previousStage) {
        this.messageId = messageId;
        this.shelter = shelter;
        this.currentStage = currentStage;
        this.previousStage = previousStage;
        this.lastUpdate = LocalDateTime.now();
    }

    public Dialog() {
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

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        this.lastUpdate = LocalDateTime.now();
    }

    public Stage getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }
}