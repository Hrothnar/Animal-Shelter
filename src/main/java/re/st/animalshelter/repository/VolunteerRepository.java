package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
