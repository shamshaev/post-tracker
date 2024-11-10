package shamshaev.code.mapper;

import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import shamshaev.code.exception.ResourceNotFoundException;
import shamshaev.code.model.BaseEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        if (id != null && entityManager.find(entityClass, id) == null) {
            throw new ResourceNotFoundException(entityClass.getSimpleName() + " with id '" + id + "' not found");
        }
        return id != null ? entityManager.find(entityClass, id) : null;
    }
}
