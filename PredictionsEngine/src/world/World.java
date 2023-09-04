package world;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.grid.Grid;
import world.rules.Rules;
import world.rules.rule.action.api.ActionContext;
import world.termination.api.Termination;

import java.util.*;

public class World {
    private Environment environment;
    private Map<String, EntitiesDefinition> entitiesMap = new HashMap<>();
    private Rules rules;
    private Termination termination;
    private Grid grid;

    public void simulateThisTick(int tickNumber) throws Exception {
        ActionContext actionContext = new ActionContext(this, tickNumber);
        try {
            actionContext.setTick(tickNumber);
            moveEntities();
            rules.simulateRules(tickNumber, actionContext);
        } catch (Exception e) {
            throw new Exception("In Tick Number: " + tickNumber + " Error: " + e.getMessage(), e);
        }
    }

    public void moveEntities() {
        for (EntitiesDefinition def : entitiesMap.values()) {
            for (EntityInstance instance : def.getEntities().values()) {
                moveEntity(instance);
            }
        }
    }

    public void moveEntity(EntityInstance entity) {
        // Generate a list of possible moves (up, down, left, right)
        List<Grid.Direction> possibleDirections = Arrays.asList(Grid.Direction.UP, Grid.Direction.DOWN, Grid.Direction.LEFT, Grid.Direction.RIGHT);
        Collections.shuffle(possibleDirections); // To randomize the move direction.

        for (Grid.Direction direction : possibleDirections) {
            int newRow = (entity.getRow() + direction.getRowChange() + grid.getRows()) % grid.getRows(); // wrap-around logic
            int newCol = (entity.getCol() + direction.getColChange() + grid.getCols()) % grid.getCols(); // wrap-around logic

            if (grid.isCellFree(newRow, newCol)) {
                // Remove from old position
                grid.removeEntityFromGrid(entity);

                // Update entity position
                entity.setPosition(newRow, newCol);

                // Add to new position
                grid.addEntityToGrid(entity);

                return; // Successfully moved, so exit the loop.
            }
        }
        // If you've reached here, then the entity couldn't move this tick.
    }


    public void addEntitiesDefinition(String entityName, EntitiesDefinition entitiesDefinition) {
        entitiesMap.put(entityName, entitiesDefinition);
    }

    public EntitiesDefinition getEntitiesDefinition(String entityName) {
        return entitiesMap.get(entityName);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Map<String, EntitiesDefinition> getEntitiesMap() {
        return entitiesMap;
    }

    public void setEntitiesMap(Map<String, EntitiesDefinition> entitiesMap) {
        this.entitiesMap = entitiesMap;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public String toString() {
        return "World{" + "environment=" + environment + ", entities=" + entitiesMap + ", rules=" + rules + ", termination=" + termination + '}';
    }
}
