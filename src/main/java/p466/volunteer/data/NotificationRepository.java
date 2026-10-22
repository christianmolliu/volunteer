package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.Notification;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findAllByUserId(long userId);
}
