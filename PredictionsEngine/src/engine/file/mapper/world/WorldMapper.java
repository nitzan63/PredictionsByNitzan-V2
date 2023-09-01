package engine.file.mapper.world;

import engine.file.mapper.world.rules.RulesMapper;
import engine.file.mapper.world.entities.EntitiesMapper;
import engine.file.mapper.world.environment.EnvMapper;
import engine.file.mapper.world.termination.TerminationMapper;
import scheme.v1.generated.PRDWorld;
import world.World;

public class WorldMapper {
    public static World mapWorld (PRDWorld jaxbWorld){
        World world = new World();
        world.setEntitiesMap(EntitiesMapper.mapEntities(jaxbWorld.getPRDEntities()));
        world.setEnvironment(EnvMapper.mapEnvironment(jaxbWorld.getPRDEvironment()));
        world.setTermination(TerminationMapper.mapTermination(jaxbWorld.getPRDTermination()));
        world.setRules(RulesMapper.mapRules(jaxbWorld.getPRDRules(), world.getEntitiesMap()));
        return world;
    }
}
