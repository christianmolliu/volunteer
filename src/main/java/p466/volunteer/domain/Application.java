package p466.volunteer.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class Application {

    @Id
    private long id;

    @NotNull(message = "User Id cannot be blank")
    private long userId;

    @NotNull(message = "Event Id cannot be blank")
    private long eventId;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private String status; // pending, approved, rejected
}
