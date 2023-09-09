package engine.file.mapper.world.entities;

import engine.file.mapper.world.entities.entity.EntityMapper;
import scheme.generated.PRDEntities;
import scheme.generated.PRDEntity;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntitiesMapper {
    public static Map<String, EntitiesDefinition> mapEntities(PRDEntities jaxbEntities) {
        List<PRDEntity> jaxbEntityList = jaxbEntities.getPRDEntity();
        // create Map of entities definition to return:
        Map<String, EntitiesDefinition> entitiesMap = new HashMap<>();
        // iterate over the entity types in the xml:
        for (PRDEntity jaxbEntity : jaxbEntityList) {
            String name = jaxbEntity.getName();
            // create prototype entity definition with population 0 (the user chooses the population
            EntitiesDefinition entityDefinition = new EntitiesDefinition(name, 0);
            // map a prototype entity instance:
            EntityInstance entityInstancePrototype = EntityMapper.mapEntity(jaxbEntity, 0, name);
            // add the prototype instance to the definition:
            //entityDefinition.addEntity(entityInstancePrototype, 0);
            entityDefinition.setPrototypeEntity(entityInstancePrototype);
            // put the entity definition in the map of entities:
            entitiesMap.put(name, entityDefinition);
        }

        return entitiesMap;
    }
}
