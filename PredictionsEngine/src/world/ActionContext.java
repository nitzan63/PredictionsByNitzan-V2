package world;

import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.grid.Grid;
import world.rules.Rules;
import world.termination.api.Termination;

import java.util.Map;

public class ActionContext {
    private Environment environment;
    private Map<String, EntitiesDefinition> entitiesMap;
    private Grid grid;
    private int tick;

    public ActionContext(World world, int tick) {
        this.entitiesMap = world.getEntitiesMap();
        this.environment = world.getEnvironment();
        this.grid = world.getGrid();
        this.tick = tick;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Map<String, EntitiesDefinition> getEntitiesMap() {
        return entitiesMap;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
