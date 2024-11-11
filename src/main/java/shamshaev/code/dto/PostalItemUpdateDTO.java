package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostalItemUpdateDTO {

    private JsonNullable<String> postalId;

    private JsonNullable<String> type;

    private JsonNullable<String> recipientPostCode;

    private JsonNullable<String> recipientAddress;

    private JsonNullable<String> recipientName;
}
