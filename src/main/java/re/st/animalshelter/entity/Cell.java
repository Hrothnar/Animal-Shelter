package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;

@Entity(name = "storage")
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Button button;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shelter shelter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private boolean owner;

    @Column(length = 4048)
    private String text;

    @Column(length = 4048)
    private byte[] photo;

    public Cell(Button button, Shelter shelter, Status status, boolean owner, String text) {
        this.button = button;
        this.shelter = shelter;
        this.status = status;
        this.owner = owner;
        this.text = text;
    }

    public Cell(Button button, Shelter shelter, Status status, boolean owner, String text, byte[] photo) {
        this.button = button;
        this.shelter = shelter;
        this.status = status;
        this.owner = owner;
        this.text = text;
        this.photo = photo;
    }

    public Cell(Button button, Shelter shelter, Status status, boolean owner, byte[] photo) {
        this.button = button;
        this.shelter = shelter;
        this.status = status;
        this.owner = owner;
        this.photo = photo;
    }

    public Cell() {

    }

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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
