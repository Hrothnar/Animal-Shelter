package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.model.entity.Dog;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}
