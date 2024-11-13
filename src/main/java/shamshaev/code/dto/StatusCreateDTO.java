package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusCreateDTO {
    @NotBlank
    private String type;

    @NotBlank
    private String postalId;

    @NotBlank
    private String postCode;
}
