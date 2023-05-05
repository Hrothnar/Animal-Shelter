package re.st.animalshelter.entity.animal;

import re.st.animalshelter.enumeration.breed.DogBreed;

import javax.persistence.*;

@Entity(name = "dogs")
public class Dog extends Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
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

    @Override
    public String getBreedAsString() {
        return breed.getText();
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
}
