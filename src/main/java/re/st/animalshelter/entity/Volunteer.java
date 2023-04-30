package re.st.animalshelter.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(unique = true)
    private long userChatId;

    @Column(length = 56)
    private String fullName;

//    @ManyToMany(fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();
//
//    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, mappedBy = "volunteer")
//    private Set<Animal> animals = new HashSet<>();

//    public void addAnimal(Animal animal) {
//        this.animals.add(animal);
//        animal.setVolunteer(this);
//    }
//
//    public void removeAnimal(Animal animal) {
//        this.animals.remove(animal);
//        animal.setVolunteer(null);
//    }
//
//    public Set<Animal> getAnimals() {
////        return Collections.unmodifiableCollection(animals);
//        return animals;
//    }
//
//    public Set<User> getUsers() {
////        return Collections.unmodifiableCollection(users);
//        return users;
//    }

    public long getId() {
        return id;
    }

    public long getUserChatId() {
        return userChatId;
    }

    public void setUserChatId(long chatId) {
        this.userChatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
