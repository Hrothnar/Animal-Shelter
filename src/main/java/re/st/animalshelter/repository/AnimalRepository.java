package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Animal;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
