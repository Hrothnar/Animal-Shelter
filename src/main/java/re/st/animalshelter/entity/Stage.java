package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Status;

import javax.persistence.*;

@Entity(name = "stages")
public class Stage {

    @Id
    private long id;

    @Enumerated(value = EnumType.STRING)
    private Status contactInfoStatus;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    private User user;

    public long getId() {
        return id;
    }

    public Status getContactInfoStatus() {
        return contactInfoStatus;
    }

    public void setContactInfoStatus(Status contactInfoStatus) {
        this.contactInfoStatus = contactInfoStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stage(Status contactInfoStatus) {
        this.contactInfoStatus = contactInfoStatus;
    }

    public Stage() {

    }

    @Override
    public String toString() {
        return "Stage{" +
                "contactInfoStatus=" + contactInfoStatus +
                '}';
    }
}
