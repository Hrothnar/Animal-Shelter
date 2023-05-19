package re.st.animalshelter.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Position;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private long chatId;
    @Column(name = "companion_chat_id")
    private long companionChatId;
    @Column(length = 56)
    private String fullName;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    @Column(length = 16)
    private String phoneNumber;
    @Column(unique = true, length = 16)
    private String passport;
    @Column(nullable = false)
    private boolean owner;
    @Column(nullable = false)
    private String currentCode;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Position position;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Stage stage;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user")
    @LazyCollection(value = LazyCollectionOption.TRUE)
    private Set<Animal> animals = new HashSet<>();
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @LazyCollection(value = LazyCollectionOption.TRUE)
    private Set<Action> actions = new HashSet<>();
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    @LazyCollection(value = LazyCollectionOption.TRUE)
    private Set<Report> reports = new HashSet<>();

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.setUser(this);
    }

    public void removeAnimal(Animal animal) {
        this.animals.remove(animal);
        animal.setUser(null);
    }

    public void addAction(Action action) {
        this.actions.add(action);
        action.setUser(this);
    }

    public void removeAction(Action action) {
        this.actions.remove(action);
        action.setUser(null);
        System.out.println(action.getUser());
    }

    public void addStage(Stage stage) {
        this.setStage(stage);
        stage.setUser(this);
    }

    public void addReport(Report report) {
        Animal animal = report.getAnimal();
        this.reports.add(report);
        animal.getReports().add(report);
        report.setUser(this);
        report.setAnimal(animal);
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public Set<Animal> getActiveAnimals() {
        return animals.stream().filter(Animal::isActive).collect(Collectors.toSet());
    }

    public Set<Action> getActions() {
        return actions;
    }

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentStatus) {
        this.currentCode = currentStatus;
    }

    public long getCompanionChatId() {
        return companionChatId;
    }

    public void setCompanionChatId(long volunteerChatId) {
        this.companionChatId = volunteerChatId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Set<Report> getReports() {
        return reports;
    }
}