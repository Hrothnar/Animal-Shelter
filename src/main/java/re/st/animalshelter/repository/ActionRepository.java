package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.st.animalshelter.entity.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
//    List<Dialog> getDialogByMessageId(int messageId);
    Action getTopByMessageId(int messageId);

}
