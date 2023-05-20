package re.st.animalshelter.dto.animal;

import re.st.animalshelter.entity.animal.Dog;
import re.st.animalshelter.enumeration.breed.DogBreed;
import re.st.animalshelter.utility.Distributor;

import java.util.Objects;
import java.util.regex.Pattern;

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

    public String getGeneralInfo() {
        String years = Distributor.conjugate(this.age);
        return breed.getText() + ", возраст: " + age + years;
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
