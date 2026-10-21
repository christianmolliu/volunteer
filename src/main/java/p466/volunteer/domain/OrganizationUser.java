package p466.volunteer.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ORGANIZATION_USERS")
public class OrganizationUser {

    @Id
    private Long id;

    private Long organizationId;
    private Long userId;
}
