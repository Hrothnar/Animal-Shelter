package re.st.animalshelter.entity;

import re.st.animalshelter.entity.animal.Animal;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String path;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private Animal animal;

    public Report(String path, User user, Animal animal) {
        this.path = path;
        this.user = user;
        this.animal = animal;
        this.time = LocalDateTime.now();
    }

    public Report() {

    }

    public long getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.time = LocalDateTime.now();
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
