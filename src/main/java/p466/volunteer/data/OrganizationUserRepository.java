package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.OrganizationUser;

import java.util.List;

public interface OrganizationUserRepository extends CrudRepository<OrganizationUser, Long> {
    boolean existsByOrganizationIdAndUserId(Long organizationId, Long userId);
    Iterable<Long> findAllByUserId(Long userId);

    boolean existsByUserId(Long userId);

    List<OrganizationUser> findAllByOrganizationId(long orgId);
}
