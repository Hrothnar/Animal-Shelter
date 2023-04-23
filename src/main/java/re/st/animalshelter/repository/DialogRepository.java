package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.st.animalshelter.model.entity.Dialog;
import re.st.animalshelter.model.entity.User;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    Dialog getDialogByMessageId(int messageId);

}
