package engine.mapper.world;

import engine.mapper.world.entities.EntitiesMapper;
import engine.mapper.world.entities.entity.EntityMapper;
import engine.mapper.world.environment.EnvMapper;
import scheme.generated.PRDWorld;
import world.World;

public class WorldMapper {
    public static World mapWorld (PRDWorld jaxbWorld){
        World world = new World();
        world.setEntities(EntitiesMapper.mapEntities(jaxbWorld.getPRDEntities()));
        world.setEnvironment(EnvMapper.mapEnvironment(jaxbWorld.getPRDEvironment()));
        // TODO after implementing rules and termination - finish.
        world.setRules(null);
        world.setTermination(null);
        return world;
    }
}
