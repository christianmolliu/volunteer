package p466.volunteer.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

@Data
@Table("event")
public class Event {

    @Id
    private long id;

    @NotBlank(message = "Event must include a title")
    private String title;

    @NotBlank(message = "Event must include a description")
    private String description;

    @NotNull(message = "Start date is required")
    private Date startDate;

    // not required. Defaults to start date?
    private Date endDate;

    @NotNull(message = "Approximate length of event in hours")
    @DecimalMin(value="0.5", message="Duration of Event must be at least a half an hour")
    private Double approxHours;

    @NotBlank(message = "Location is required")
    private String location;

    private Long userId;

    private Long organizationId;

}
