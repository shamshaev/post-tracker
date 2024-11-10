package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOfficeCreateDTO {

    private String postCode;

    private String name;

    private String address;
}
