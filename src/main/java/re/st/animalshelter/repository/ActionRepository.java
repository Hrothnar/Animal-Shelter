package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    @Query(value = "SELECT a FROM actions a WHERE a.messageId = :messageId AND a.id = (SELECT MAX(id) FROM actions WHERE messageId = :messageId)")
    Optional<Action> findLastActionByMessageId(@Param("messageId") int messageId);
//
//    @Query(value = "SELECT a FROM actions a WHERE a.messageId = :messageId AND a.id IN (SELECT TOP (2, a2.id) FROM actions a2 WHERE a2.messageId = :messageId ORDER BY a2.id DESC)")
//    LinkedList<Action> findTwoLastActionsByMessageId(@Param("messageId") int messageId);
}
