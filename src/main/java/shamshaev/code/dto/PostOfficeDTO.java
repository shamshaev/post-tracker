package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOfficeDTO {

    private Long id;

    private String postCode;

    private String name;

    private String address;

    private String createdAt;
}
