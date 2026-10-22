package p466.volunteer.data;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.repository.CrudRepository;
import p466.volunteer.domain.Organization;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    boolean existsByName(String name);

    Organization getByName(String name);
}
