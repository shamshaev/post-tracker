package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostalItemCreateDTO {

    private String postalId;

    private String type;

    private String recipientPostCode;

    private String recipientAddress;

    private String recipientName;
}
