package engine.mapper.world;

import engine.mapper.world.entities.EntitiesMapper;
import engine.mapper.world.entities.entity.EntityMapper;
import engine.mapper.world.environment.EnvMapper;
import engine.mapper.world.rules.RulesMapper;
import engine.mapper.world.termination.TerminationMapper;
import scheme.generated.PRDWorld;
import world.World;

public class WorldMapper {
    public static World mapWorld (PRDWorld jaxbWorld){
        World world = new World();
        world.setEntities(EntitiesMapper.mapEntities(jaxbWorld.getPRDEntities()));
        world.setEnvironment(EnvMapper.mapEnvironment(jaxbWorld.getPRDEvironment()));
        world.setTermination(TerminationMapper.mapTermination(jaxbWorld.getPRDTermination()));
        // TODO after implementing rules and termination - finish.
        world.setRules(RulesMapper.mapRules(jaxbWorld.getPRDRules(), world.getEntities()));
        return world;
    }
}
