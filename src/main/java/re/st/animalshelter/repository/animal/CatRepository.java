package re.st.animalshelter.repository.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.animal.Cat;

import java.util.List;
import java.util.Set;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findAllByActiveIsTrueAndUserIsNull();

}
