package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;

@Entity(name = "storage")
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Button button;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shelter shelter;

    private boolean owner;

    private String text;

    private String photo;

    public long getId() {
        return id;
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

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
