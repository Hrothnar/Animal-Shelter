package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.animal.Breed;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "animals1")
public class Animal1 {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
    private String animal;

    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
    private String breed;

//    @Enumerated(value = EnumType.STRING)
    private String reportStatus;

    private LocalDateTime expirationDate;

    private LocalDateTime lastReportDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getLastReportDate() {
        return lastReportDate;
    }

    public void setLastReportDate(LocalDateTime lastReportDate) {
        this.lastReportDate = lastReportDate;
    }
}
