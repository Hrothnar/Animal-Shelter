package re.st.animalshelter.entity;

import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "text_path")
    private String textPath;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private Animal animal;


    public long getId() {
        return id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.time = LocalDateTime.now();
        this.photoPath = photoPath;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.time = LocalDateTime.now();
        this.textPath = textPath;
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

    public LocalDateTime getTime() {
        return time;
    }

    public String getTimeAsString() {
        int dayOfMonth = time.getDayOfMonth();
        int monthValue = time.getMonthValue();
        int year = time.getYear();
        return dayOfMonth + "." + monthValue + "." + year;
    }

    public String getAnimalData() {
        String breedAsString = animal.getBreedAsString();
        int age = animal.getAge();
        return breedAsString + " -- " + age + " y.o.";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusAsString() {
        if (this.status == Status.NONE) {
            return "Не просмотрен";
        } else if (this.status == Status.PASSED) {
            return "Принят";
        } else {
            return "Завален";
        }
    }
}
