package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Status;

import javax.persistence.*;

@Entity(name = "stages")
public class Stage {

    @Id
    private long id;

    @Column(name = "contact_info_status")
    @Enumerated(value = EnumType.STRING)
    private Status contactInfoStatus;

    @Column(name = "dialog_status")
    @Enumerated(value = EnumType.STRING)
    private Status dialogStatus;

    @OneToOne(cascade = {}, fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    private User user;


    public Stage(Status contactInfoStatus, Status dialogStatus) {
        this.contactInfoStatus = contactInfoStatus;
        this.dialogStatus = dialogStatus;
    }

    public Stage() {

    }

    public long getId() {
        return id;
    }

    public Status getContactInfoStatus() {
        return contactInfoStatus;
    }

    public void setContactInfoStatus(Status contactInfoStatus) {
        this.contactInfoStatus = contactInfoStatus;
    }

    public Status getDialogStatus() {
        return dialogStatus;
    }

    public void setDialogStatus(Status dialogStatus) {
        this.dialogStatus = dialogStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
