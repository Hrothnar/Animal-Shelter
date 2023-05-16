package re.st.animalshelter.dto.animal;

import re.st.animalshelter.entity.animal.Dog;
import re.st.animalshelter.enumeration.breed.DogBreed;

import java.util.Objects;

public class DogDTO {
    private final long id;
    private final int age;
    private final DogBreed breed;

    public DogDTO(Dog dog) {
        this.id = dog.getId();
        this.age = dog.getAge();
        this.breed = dog.getBreed();
    }

    public long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public DogBreed getBreed() {
        return breed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogDTO catDTO = (DogDTO) o;
        return age == catDTO.age && breed == catDTO.breed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, breed);
    }
}
