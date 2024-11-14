package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TrackStatusUpdateDTO {
    @NotBlank
    private JsonNullable<String> type;

    @NotBlank
    private JsonNullable<String> postalId;

    @NotBlank
    private JsonNullable<String> postCode;
}
