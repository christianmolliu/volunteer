package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.UserRole;

import java.util.List;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
     List<UserRole> findAllByUserId(Long id);
}
