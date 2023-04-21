package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByChatId(long chatId);
}
