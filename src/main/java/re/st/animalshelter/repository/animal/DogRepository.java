package re.st.animalshelter.repository.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.entity.animal.Dog;

import java.util.List;
import java.util.Set;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findAllByActiveIsTrueAndUserIsNull();
    List<Dog> findAllByActiveIsTrueAndUserNotNullAndVolunteerIsNull();
    List<Dog> findAllByActiveIsTrueAndUserIsNullAndVolunteerIsNull();



}
