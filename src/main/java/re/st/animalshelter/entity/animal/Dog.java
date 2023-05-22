package re.st.animalshelter.entity.animal;

import re.st.animalshelter.enumeration.breed.DogBreed;
import re.st.animalshelter.utility.Distributor;

import javax.persistence.*;

@Entity(name = "dogs")
public class Dog extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "breed", nullable = false, length = 32)
    @Enumerated(value = EnumType.STRING)
    private DogBreed breed;

    public Dog(int age, DogBreed breed) {
        this.age = age;
        this.breed = breed;
    }

    public Dog() {

    }

    @Override
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

    public DogBreed getBreed() {
        return breed;
    }

    public void setBreed(DogBreed breed) {
        this.breed = breed;
    }

    @Override
    public String getGeneralInfo() {
        String years = Distributor.conjugate(this.age);
        return breed.getText() + ", возраст: " + age + years;
    }
}
