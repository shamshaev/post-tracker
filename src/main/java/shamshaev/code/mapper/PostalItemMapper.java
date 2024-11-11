package shamshaev.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import shamshaev.code.dto.PostalItemDTO;
import shamshaev.code.dto.PostalItemUpdateDTO;
import shamshaev.code.dto.PostalItemCreateDTO;
import shamshaev.code.model.PostalItem;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, TimeMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostalItemMapper {
    public abstract PostalItem map(PostalItemCreateDTO dto);

    public abstract PostalItemDTO map(PostalItem model);

    public abstract PostalItem map(PostalItemDTO dto);

    public abstract void update(PostalItemUpdateDTO dto, @MappingTarget PostalItem model);
}
