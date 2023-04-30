package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Animal1;

@Repository
public interface Animal1Repository extends JpaRepository<Animal1, Long> {
    Animal1 getById(long id);
}
