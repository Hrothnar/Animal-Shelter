package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Action;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    @Query(value = "SELECT a FROM actions a WHERE a.messageId = :messageId AND a.id = (SELECT MAX(id) FROM actions WHERE messageId = :messageId)")
    Optional<Action> findLastActionByMessageId(@Param("messageId") int messageId);
}
