package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
}
