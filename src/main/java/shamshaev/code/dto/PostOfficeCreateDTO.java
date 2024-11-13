package shamshaev.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOfficeCreateDTO {
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$")
    private String postCode;

    @NotBlank
    private String name;

    @NotBlank
    private String address;
}
