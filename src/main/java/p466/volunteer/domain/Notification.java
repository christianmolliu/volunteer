package p466.volunteer.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table
public class Notification {

    @Id
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private boolean isRead;

    @NotNull
    private long userId;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

}
