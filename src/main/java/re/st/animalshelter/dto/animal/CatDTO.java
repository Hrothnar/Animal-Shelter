package re.st.animalshelter.dto.animal;

import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.enumeration.breed.CatBreed;

import java.util.Objects;

public class CatDTO {
    private final long id;
    private final int age;
    private final CatBreed breed;

    public CatDTO(Cat cat) {
        this.id = cat.getId();
        this.age = cat.getAge();
        this.breed = cat.getBreed();
    }

    public long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public CatBreed getBreed() {
        return breed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatDTO catDTO = (CatDTO) o;
        return age == catDTO.age && breed == catDTO.breed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, breed);
    }
}
