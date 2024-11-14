package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackStatusDTO {

    private Long id;

    private String type;

    private String postalId;

    private String postCode;

    private String createdAt;
}
