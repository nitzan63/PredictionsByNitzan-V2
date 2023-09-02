package engine.file.mapper.world;

import engine.file.mapper.world.grid.GridMapper;
import engine.file.mapper.world.rules.RulesMapper;
import engine.file.mapper.world.entities.EntitiesMapper;
import engine.file.mapper.world.environment.EnvMapper;
import engine.file.mapper.world.termination.TerminationMapper;
import scheme.generated.PRDWorld;
import world.World;

public class WorldMapper {
    public static World mapWorld (PRDWorld jaxbWorld){
        World world = new World();
        world.setGrid(GridMapper.mapGrid(jaxbWorld.getPRDGrid()));
        world.setEntitiesMap(EntitiesMapper.mapEntities(jaxbWorld.getPRDEntities()));
        world.setEnvironment(EnvMapper.mapEnvironment(jaxbWorld.getPRDEnvironment()));
        world.setTermination(TerminationMapper.mapTermination(jaxbWorld.getPRDTermination()));
        world.setRules(RulesMapper.mapRules(jaxbWorld.getPRDRules(), world.getEntitiesMap()));
        return world;
    }
}
