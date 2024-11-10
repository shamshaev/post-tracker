package shamshaev.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostOfficeUpdateDTO {

    private JsonNullable<String> postCode;

    private JsonNullable<String> name;

    private JsonNullable<String> address;
}
