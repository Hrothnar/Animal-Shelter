package re.st.animalshelter.entity;

import javax.persistence.*;

@Entity(name = "stages")
public class Stage {
    @Id
    private long id;
    @Column(name = "contact_info_code")
    private String contactInfoCode;
    @Column(name = "dialog_code")
    private String dialogCode;
    @OneToOne(cascade = {}, fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    private User user;

    public Stage(String contactInfoCode, String dialogCode) {
        this.contactInfoCode = contactInfoCode;
        this.dialogCode = dialogCode;
    }

    public Stage() {

    }

    public long getId() {
        return id;
    }

    public String getContactInfoCode() {
        return contactInfoCode;
    }

    public void setContactInfoCode(String contactInfoStatus) {
        this.contactInfoCode = contactInfoStatus;
    }

    public String getDialogCode() {
        return dialogCode;
    }

    public void setDialogCode(String dialogStatus) {
        this.dialogCode = dialogStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
