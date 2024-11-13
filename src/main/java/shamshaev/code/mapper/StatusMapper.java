package shamshaev.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import shamshaev.code.dto.StatusCreateDTO;
import shamshaev.code.dto.StatusDTO;
import shamshaev.code.dto.StatusUpdateDTO;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.model.PostOffice;
import shamshaev.code.model.PostalItem;
import shamshaev.code.model.Status;
import shamshaev.code.repository.PostOfficeRepository;
import shamshaev.code.repository.PostalItemRepository;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, TimeMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class StatusMapper {
    private PostOfficeRepository postOfficeRepository;
    private PostalItemRepository postalItemRepository;

    @Autowired
    public void setPostOfficeRepository(PostOfficeRepository postOfficeRepository) {
        this.postOfficeRepository = postOfficeRepository;
    }

    @Autowired
    public void setPostalItemRepository(PostalItemRepository postalItemRepository) {
        this.postalItemRepository = postalItemRepository;
    }

    @Mapping(source = "postCode", target = "postOffice", qualifiedByName = "postCodeToPostOffice")
    @Mapping(source = "postalId", target = "postalItem", qualifiedByName = "postalIdToPostalItem")
    public abstract Status map(StatusCreateDTO dto);

    @Mapping(source = "postOffice.postCode", target = "postCode")
    @Mapping(source = "postalItem.postalId", target = "postalId")
    public abstract StatusDTO map(Status model);

    @Mapping(source = "postCode", target = "postOffice", qualifiedByName = "postCodeToPostOffice")
    @Mapping(source = "postalId", target = "postalItem", qualifiedByName = "postalIdToPostalItem")
    public abstract void update(StatusUpdateDTO dto, @MappingTarget Status model);

    @Named("postCodeToPostOffice")
    public PostOffice postCodeToPostOffice(String postCode) {
        return postOfficeRepository.findByPostCode(postCode)
                .orElseThrow(() -> new ResourceNotFoundException("PostOffice with postcode '" + postCode
                        + "' not found"));
    }

    @Named("postalIdToPostalItem")
    public PostalItem postalIdToPostalItem(String postalId) {
        return postalItemRepository.findByPostalId(postalId)
                .orElseThrow(() -> new ResourceNotFoundException("PostalItem with postalId '" + postalId
                        + "' not found"));
    }
}
