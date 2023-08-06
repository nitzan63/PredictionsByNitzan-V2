package engine.mapper.world.entities;

import engine.mapper.world.entities.entity.EntityMapper;
import scheme.generated.PRDEntities;
import scheme.generated.PRDEntity;
import world.entities.Entities;
import world.entities.entity.Entity;

import java.util.List;
import java.util.Map;

public class EntitiesMapper {
    public static Entities mapEntities (PRDEntities jaxbEntities) {
        List<PRDEntity> jaxbEntityList = jaxbEntities.getPRDEntity();
        Integer population = jaxbEntityList.get(0).getPRDPopulation();
        String name = jaxbEntityList.get(0).getName();
        Entities entities = new Entities(name, population);
        int serialNumber = 1;

        for (PRDEntity jaxbEntity : jaxbEntityList) {
            Entity entity = EntityMapper.mapEntity(jaxbEntity, serialNumber);
            entities.addEntity(entity, serialNumber);
            serialNumber++;
        }

        return entities;
    }
}
