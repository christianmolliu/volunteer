package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.Event;

public interface EventRepository extends CrudRepository<Event, Long> {

}
