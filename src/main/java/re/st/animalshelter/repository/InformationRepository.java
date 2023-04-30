package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Shelter;

public interface InformationRepository extends JpaRepository<Cell, Long> {
    Cell getByButtonAndShelterAndOwner(Button button, Shelter shelter, boolean owner);
    Cell getByButtonAndOwner(Button button, boolean owner);
    Cell getByButtonAndShelter(Button button, Shelter shelter);
    Cell getByButton(Button button);
}
