package shamshaev.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import shamshaev.code.dto.PostalItemDTO;
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.dto.PostalItemCreateDTO;
import shamshaev.code.model.PostalItem;
import shamshaev.code.model.Status;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, TimeMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostalItemMapper {
    public abstract PostalItem map(PostalItemCreateDTO dto);

    @Mapping(target = "statuses", qualifiedByName = "statusToString")
    public abstract PostalItemDTO map(PostalItem model);

    public abstract void update(PostalItemUpdateDTO dto, @MappingTarget PostalItem model);

    @Named("statusToString")
    public String statusToString(Status status) {
        var joiner = new StringJoiner(", ");
        joiner.add(status.getType().toString());
        joiner.add(status.getPostOffice().getPostCode());
        joiner.add(status.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm")));
        return joiner.toString();
    }
}
