package re.st.animalshelter.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique = true)
    private long chatId;

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

    private boolean owner;

//    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user")
//    private Stage stage;
//
//    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "users")
//    private Set<Volunteer> volunteers = new HashSet<>();
//
//    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "user")
//    private Set<Animal> animals = new HashSet<>();
//
//    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "user")
//    private Set<Action> actions = new HashSet<>();

//    public void addVolunteer(Volunteer volunteer) {
//        this.volunteers.add(volunteer);
//        volunteer.getUsers().add(this);
//    }
//
//    private void removeVolunteer(Volunteer volunteer) {
//        this.volunteers.remove(volunteer);
//        volunteer.getUsers().remove(this);
//    }
//
//    public void addAnimal(Animal animal) {
//        this.animals.add(animal);
//        animal.setUser(this);
//    }
//
//    public void removeAnimal(Animal animal) {
//        this.animals.remove(animal);
//        animal.setUser(null);
//    }
//
//    public void addAction(Action action) {
//        this.actions.add(action);
//        action.setUser(this);
//    }
//
//    public void removeAction(Action action) {
//        this.actions.remove(action);
//        action.setUser(null);
//    }

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

//    public Stage getStage() {
//        return stage;
//    }
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
//
//    public Set<Volunteer> getVolunteers() {
////        return Collections.unmodifiableCollection(volunteers);
//        return volunteers;
//    }
//
//    public Set<Animal> getAnimals() {
////        return Collections.unmodifiableCollection(animals);
//        return animals;
//    }
//
//    public Set<Action> getActions() {
////        return Collections.unmodifiableCollection(actions);
//        return actions;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
//                ", fullName='" + fullName + '\'' +
//                ", userName='" + userName + '\'' +
//                ", email='" + email + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", passport='" + passport + '\'' +
//                ", owner=" + owner +
//                ", stage=" + stage +
//                ", volunteers=" + volunteers +
//                ", animals=" + animals +
//                ", actions=" + actions +
                '}';
    }
}