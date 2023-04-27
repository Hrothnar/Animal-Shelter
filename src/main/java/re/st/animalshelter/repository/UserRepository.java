package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByChatId(long chatId);
    User getUserById(long id);
    User getUserByUserNameOrEmailOrPassport(String userName, String email, String passport);
}
