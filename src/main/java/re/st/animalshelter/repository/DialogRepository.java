package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.st.animalshelter.model.entity.Dialog;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    Dialog getDialogByMessageId(int messageId);

}
