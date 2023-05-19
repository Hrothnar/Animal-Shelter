package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.shelter.Shelter;

import javax.persistence.*;

@Entity(name = "storage")
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Shelter shelter;
    @Column(nullable = false)
    private boolean owner;
    @Column(length = 4096)
    private String text;
    @Column(length = 4096)
    private byte[] photo;

    public Cell(String code, Shelter shelter,  boolean owner, String text, byte[] photo) {
        this.code = code;
        this.shelter = shelter;
        this.owner = owner;
        this.text = text;
        this.photo = photo;
    }

    public Cell() {

    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String button) {
        this.code = button;
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
}
