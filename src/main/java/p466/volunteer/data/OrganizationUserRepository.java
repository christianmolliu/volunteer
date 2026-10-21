package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.OrganizationUser;

public interface OrganizationUserRepository extends CrudRepository<OrganizationUser, Long> {
    boolean existsByOrganizationIdAndUserId(Long organizationId, Long userId);
}
