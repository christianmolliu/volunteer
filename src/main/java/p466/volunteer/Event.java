package p466.volunteer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

@Data
@Table
public class Event {

    @Id
    private long id;

    @NotNull(message = "Event must include a title")
    private String title;

    @NotNull(message = "Event must include a description")
    private String description;

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

}
