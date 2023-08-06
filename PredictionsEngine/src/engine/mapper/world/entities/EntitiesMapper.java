package engine.mapper.world.entities;

import engine.mapper.world.entities.entity.EntityMapper;
import scheme.generated.PRDEntities;
import scheme.generated.PRDEntity;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;

import java.util.List;

public class EntitiesMapper {
    public static EntitiesDefinition mapEntities (PRDEntities jaxbEntities) {
        List<PRDEntity> jaxbEntityList = jaxbEntities.getPRDEntity();
        Integer population = jaxbEntityList.get(0).getPRDPopulation();
        String name = jaxbEntityList.get(0).getName();
        EntitiesDefinition entities = new EntitiesDefinition(name, population);
        int serialNumber = 1;

        for (PRDEntity jaxbEntity : jaxbEntityList) {
            EntityInstance entity = EntityMapper.mapEntity(jaxbEntity, serialNumber);
            entities.addEntity(entity, serialNumber);
            serialNumber++;
        }

        return entities;
    }
}
