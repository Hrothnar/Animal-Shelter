package re.st.animalshelter.entity;

import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.animal.Breed;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Shelter animal;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Breed breed;

    @Enumerated(value = EnumType.STRING)
    private Status reportStatus;

    private LocalDateTime expirationDate;

    private LocalDateTime lastReportDate;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    public long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
//
//    public Shelter getAnimal() {
//        return animal;
//    }
//
//    public void setAnimal(Shelter animal) {
//        this.animal = animal;
//    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Status getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Status reportStatus) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public void setId(long id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "Animal{" +
//                "id=" + id +
//                ", age=" + age +
//                ", animal=" + animal +
//                ", breed=" + breed +
////                ", reportStatus=" + reportStatus +
////                ", expirationDate=" + expirationDate +
////                ", lastReportDate=" + lastReportDate +
////                ", user=" + user +
////                ", volunteer=" + volunteer +
//                '}';
//    }
}
