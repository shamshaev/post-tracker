package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostalItemDTO {

    private Long id;

    private String postalId;

    private String type;

    private String recipientPostCode;

    private String recipientAddress;

    private String recipientName;

    private List<String> statuses;

    private String updatedAt;

    private String createdAt;
}
