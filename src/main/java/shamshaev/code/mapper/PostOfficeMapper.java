package shamshaev.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import shamshaev.code.dto.PostOfficeCreateDTO;
import shamshaev.code.dto.PostOfficeDTO;
import shamshaev.code.dto.PostOfficeUpdateDTO;
import shamshaev.code.model.PostOffice;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, TimeMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostOfficeMapper {
    public abstract PostOffice map(PostOfficeCreateDTO dto);

    public abstract PostOfficeDTO map(PostOffice model);

    public abstract void update(PostOfficeUpdateDTO dto, @MappingTarget PostOffice model);
}
