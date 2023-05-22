package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Shelter;

import javax.persistence.*;

@Entity(name = "storage")
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "code", nullable = false, length = 4)
    private String code;
    @Column(name = "shelter", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private Shelter shelter;
    @Column(name = "owner", nullable = false)
    private boolean owner;
    @Column(name = "text", length = 4096)
    private String text;
    @Column(name = "photo")
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
