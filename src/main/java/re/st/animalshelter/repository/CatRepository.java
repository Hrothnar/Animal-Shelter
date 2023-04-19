package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.model.entity.Cat;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
}
