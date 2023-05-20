package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Volunteer;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findById(long id);
    Optional<Volunteer> findByChatId(long chatId);
    Optional<Volunteer> findByIdOrUserName(long id, String userName);
    List<Volunteer> findAll();
}
