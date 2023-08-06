package engine.mapper.world.entities.entity;

import engine.mapper.world.entities.entity.properties.EntityPropertiesMapper;
import scheme.generated.PRDEntity;
import world.entities.entity.Entity;
import world.entities.entity.properties.EntityProperties;

public class EntityMapper {
    public static Entity mapEntity(PRDEntity jaxbEntity, Integer serialNumber) {
        String name = jaxbEntity.getName();
        EntityProperties entityProperties = EntityPropertiesMapper.mapProperties(jaxbEntity.getPRDProperties());
        return new Entity(serialNumber, entityProperties);
    }
}
