package re.st.animalshelter.entity;

import re.st.animalshelter.entity.animal.Animal;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private long chatId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name", length = 56)
    private String fullName;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "volunteer")
    private Set<Animal> animals = new HashSet<>();


    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.setVolunteer(this);
    }

    public void removeAnimal(Animal animal) {
        this.animals.remove(animal);
        animal.setVolunteer(null);
    }

    public Set<Animal> getAnimals() {
//        return Collections.unmodifiableCollection(animals);
        return animals;
    }

    public Set<User> getUsers() {
//        return Collections.unmodifiableCollection(users);
        return users;
    }

    public long getId() {
        return id;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
