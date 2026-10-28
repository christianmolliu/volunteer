package p466.volunteer.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("organization")
public class Organization {
    @Id
    private long id;

    @NotBlank(message = "Name is required")
    private String name;
}
