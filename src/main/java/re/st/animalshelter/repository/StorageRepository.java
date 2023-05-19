package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Cell;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Cell, Long> {
    Optional<Cell> findByCode(String code);
    Optional<Cell> findByCodeAndShelter(String code, Shelter shelter);
    Optional<Cell> findByCodeAndOwner(String code, boolean owner);
    Optional<Cell> findByCodeAndShelterAndOwner(String code, Shelter shelter, boolean owner);
}
