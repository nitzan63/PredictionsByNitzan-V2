package world.rules.rule.action.api;

import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.grid.Grid;


import java.util.Map;

public class ActionContext {
    private Environment environment;
    private Map<String, EntitiesDefinition> entitiesMap;
    private Grid grid;
    private int tick;
    private EntityInstance secondaryEntityInContext;
    public ActionContext(World world, int tick) {
        this.entitiesMap = world.getEntitiesMap();
        this.environment = world.getEnvironment();
        this.grid = world.getGrid();
        this.tick = tick;
        secondaryEntityInContext = null;
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

    public void setSecondaryEntityInContext(EntityInstance secondaryEntityInContext) {
        this.secondaryEntityInContext = secondaryEntityInContext;
    }

    public EntityInstance getSecondaryEntityInContext() {
        return secondaryEntityInContext;
    }

}
