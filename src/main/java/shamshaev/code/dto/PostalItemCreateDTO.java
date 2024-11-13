package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostalItemCreateDTO {
    @NotNull
    @Pattern(regexp = "^[0-9]{14}$")
    private String postalId;

    @NotBlank
    private String type;

    @NotNull
    @Pattern(regexp = "^[0-9]{6}$")
    private String recipientPostCode;

    @NotBlank
    private String recipientAddress;

    @NotBlank
    private String recipientName;
}
