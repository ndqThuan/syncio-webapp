package online.syncio.backend.label;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LabelDTO {
        private UUID id;

        @NotNull
        private String name;

        @NotNull
        private String description;

        @NotNull
        private Double price;

        private LocalDateTime createdDate;

        private UUID createdBy;

        private String labelURL;

        @NotNull
        private StatusEnum status = StatusEnum.ENABLED;
        public String getLabelURL() {
                return labelURL = "http://localhost:8080/api/v1/posts/images/" + labelURL;
        }
}
