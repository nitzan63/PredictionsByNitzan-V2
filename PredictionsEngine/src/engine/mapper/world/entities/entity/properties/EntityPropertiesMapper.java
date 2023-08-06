package engine.mapper.world.entities.entity.properties;

import engine.mapper.world.entities.entity.properties.property.EntityPropertyMapper;
import scheme.generated.PRDProperties;
import scheme.generated.PRDProperty;
import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;

public class EntityPropertiesMapper {
    public static EntityProperties mapProperties (PRDProperties jaxbProperties){
        EntityProperties entityProperties = new EntityProperties();

        for (PRDProperty jaxbProperty : jaxbProperties.getPRDProperty()){
            EntityProperty property = EntityPropertyMapper.mapProperty(jaxbProperty);
            entityProperties.addProperty(property);
        }

        return entityProperties;
    }

}
