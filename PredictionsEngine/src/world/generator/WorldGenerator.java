package world.generator;

import dto.UserInputDTO;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.grid.Grid;

import java.util.HashMap;
import java.util.Map;

public class WorldGenerator {
    private final World prototypeWorld;
    public static final int PROTOTYPE_INSTANCE = 0;

    public WorldGenerator(World prototypeWorld) {
        this.prototypeWorld = prototypeWorld;
    }

    public World generateWorld(UserInputDTO userInput) {
        World newWorld = new World();
        // Generate Grid:
        newWorld.setGrid(new Grid(prototypeWorld.getGrid()));
        // populate world:
        newWorld.setEntitiesMap(populateWorld(userInput, newWorld));
        // Set environment
        newWorld.setEnvironment(generateEnvironment(userInput));
        // Set Rules:

        // Set termination:

        return newWorld;
    }

    private Map<String, EntitiesDefinition> populateWorld(UserInputDTO userInput, World newWorld) {
        // create entity generator instance:
        EntityGenerator entityGenerator = new EntityGenerator();
        Map<String, EntitiesDefinition> entitiesDefinitionMap = new HashMap<>();

        //
        for (Map.Entry<String, Integer> entry : userInput.getEntityPopulationMap().entrySet()) {
            String entityName = entry.getKey();
            int population = entry.getValue();

            EntitiesDefinition entitiesDefinition = new EntitiesDefinition(entityName, population);

            // get the prototype instance from the definition:
            EntityInstance prototype = prototypeWorld.getEntitiesDefinition(entityName).getEntity(PROTOTYPE_INSTANCE);

            entitiesDefinition.setPrototypeEntity(prototype);

            for (int i = 0; i < population; i++) {
                EntityInstance newInstance = entityGenerator.generateNewInstance(prototype, newWorld.getGrid(), i);
                entitiesDefinition.addEntity(newInstance, i);
            }
            entitiesDefinitionMap.put(entityName, entitiesDefinition);
        }
        return entitiesDefinitionMap;
    }
}

