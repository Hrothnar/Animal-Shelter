package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Position;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(long chatId);
    Optional<User> findUserByIdOrUserName(long id, String userName);
    Optional<User> findById(long id);
    Optional<User> findByUserName(String userName);
    Optional<User> findByCompanionChatId(long chatId);
    LinkedList<User> findAllByPosition(Position position);
    List<User> findAll();
}
