package engine.file.mapper.world.entities.entity;

import engine.file.mapper.world.entities.entity.properties.EntityPropertiesMapper;
import scheme.generated.PRDEntity;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.EntityProperties;

public class EntityMapper {
    public static EntityInstance mapEntity(PRDEntity jaxbEntity, Integer serialNumber, String entityName) {
        String name = jaxbEntity.getName();
        EntityProperties entityProperties = EntityPropertiesMapper.mapProperties(jaxbEntity.getPRDProperties());
        return new EntityInstance(serialNumber, entityProperties, 0, 0, entityName);
    }
}
