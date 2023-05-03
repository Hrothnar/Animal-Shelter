package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;

@Repository
public interface InformationRepository extends JpaRepository<Cell, Long> {
    Cell findByButtonAndShelterAndOwner(Button button, Shelter shelter, boolean owner);
    Cell findByButtonAndOwner(Button button, boolean owner);
    Cell findByButtonAndShelter(Button button, Shelter shelter);
    Cell findByButton(Button button);
}
