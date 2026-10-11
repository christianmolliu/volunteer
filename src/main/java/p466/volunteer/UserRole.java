package p466.volunteer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("USER_ROLES")
public class UserRole {

    @Id
    private Long id;

    private Long userId;

    private String role;
}
