package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostalItemUpdateDTO {

    @Pattern(regexp = "^[0-9]{14}$")
    private JsonNullable<String> postalId;

    @NotBlank
    private JsonNullable<String> type;

    @Pattern(regexp = "^[0-9]{6}$")
    private JsonNullable<String> recipientPostCode;

    @NotBlank
    private JsonNullable<String> recipientAddress;

    @NotBlank
    private JsonNullable<String> recipientName;
}
