package re.st.animalshelter.model.entity;

import re.st.animalshelter.enumeration.animal.CatBreed;
import re.st.animalshelter.model.Animal;

import javax.persistence.*;

@Entity(name = "cats")
public class Cat implements Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CatBreed breed;
    @Column(nullable = false)
    private int age;
    private boolean status;
    @OneToOne()
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CatBreed getBreed() {
        return breed;
    }

    public void setBreed(CatBreed breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
