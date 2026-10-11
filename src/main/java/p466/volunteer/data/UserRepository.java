package p466.volunteer.data;

import org.springframework.data.repository.CrudRepository;
import p466.volunteer.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
