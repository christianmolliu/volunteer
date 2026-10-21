package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.Event;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOrganizationId(Long organizationId);
}
