package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostOfficeUpdateDTO {
    @Pattern(regexp = "^[0-9]{6}$")
    private JsonNullable<String> postCode;

    @NotBlank
    private JsonNullable<String> name;

    @NotBlank
    private JsonNullable<String> address;
}
