package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import re.st.animalshelter.model.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    List<NotificationTask> findAllByNotificationDateTime(LocalDateTime localDateTime);

    //SQL запросы через Spring JPA с указанием классов и полей, в таком случае название метода значение не имеет
//    @Query("SELECT nt FROM NotificationTask nt WHERE nt.message like %:messageLike%")
//    @Query(value = "SELECT nt FROM NotificatonTask nt WHERE nt.message like %:messageLike%", nativeQuery = true)
    List<NotificationTask> findAllByMessageLike (@Param("messageLike") String message);

    List<NotificationTask> findAllByNotificationDateTimeAndChatId(LocalDateTime localDateTime, Long chatId);

    @Modifying // без этой аннотации Spring думает что запросы выполнены в нативном SQL
    @Query("DELETE FROM NotificationTask WHERE message like %:messageLike%")
    void removeAllLike(@Param("messageLike") String message);

















}
