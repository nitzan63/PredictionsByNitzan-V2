package world.generator;

import dto.ErrorDTO;
import dto.UserInputDTO;
import engine.input.validator.EnvironmentInputValidator;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;
import world.grid.Grid;
import world.property.impl.PropertyBool;
import world.property.impl.PropertyDecimal;
import world.property.impl.PropertyFloat;
import world.property.impl.PropertyString;
import world.utils.range.Range;

import java.time.LocalDateTime;
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
        newWorld.setRules(prototypeWorld.getRules());
        // Set termination:
        newWorld.setTermination(prototypeWorld.getTermination());
        return newWorld;
    }

    private Environment generateEnvironment(UserInputDTO userInputDTO){
        // validate user input:
        EnvProperties prototypeEnvProperties = prototypeWorld.getEnvironment().getProperties();
        try {
        EnvironmentInputValidator.validateInput(userInputDTO, prototypeEnvProperties);
        } catch (Exception e){
            throw e; // handle exception? TODO: after designing exception system for engine.
        }

        EnvProperties envProperties = new EnvProperties();
        Map<String , String> environmertPropMap = userInputDTO.getEnvironmertPropMap();
        for (EnvProperty property : prototypeEnvProperties.getProperties()){
            EnvProperty newProperty;
            if (environmertPropMap.get(property.getName())!= null){
                newProperty = generatePropertyWithUserInput(property, environmertPropMap.get(property.getName()));
            } else {
                newProperty = generatePropertyFromScratch(property);
            }
            envProperties.addProperty(newProperty);
        }

        Environment newEnvironment = new Environment();
        newEnvironment.setProperties(envProperties);

        return newEnvironment;
    }

    private EnvProperty generatePropertyFromScratch (EnvProperty prototypeProperty){
        String name = prototypeProperty.getName();
        String type = prototypeProperty.getType();
        Range<?> range = prototypeProperty.getRange();
        switch (type) {
            case "boolean":
                return new PropertyBool(name, true, null);

            case "decimal":
                return new PropertyDecimal(name, true, null, (Range<Integer>) range);

            case "float":
                return new PropertyFloat(name, true, null, (Range<Float>) range);

            case "string":
                return new PropertyString(name, true, null);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private EnvProperty generatePropertyWithUserInput(EnvProperty prototypeProperty, String userInputValue) {
        String name = prototypeProperty.getName();
        String type = prototypeProperty.getType();
        Range<?> range = prototypeProperty.getRange();

        Object parsedValue = EnvironmentInputValidator.parseValue(userInputValue, type);

        switch (type) {
            case "boolean":
                return new PropertyBool(name, false, (Boolean) parsedValue);

            case "decimal":
                return new PropertyDecimal(name, false, (Integer) parsedValue, (Range<Integer>) range);

            case "float":
                return new PropertyFloat(name, false, (Float) parsedValue, (Range<Float>) range);

            case "string":
                return new PropertyString(name, false, (String) parsedValue);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }



    private Map<String, EntitiesDefinition> populateWorld(UserInputDTO userInput, World newWorld) {
        // create entity generator instance:
        EntityGenerator entityGenerator = new EntityGenerator();
        Map<String, EntitiesDefinition> entitiesDefinitionMap = new HashMap<>();
        Map<String, EntitiesDefinition> prototypeEntitiesDefinitionMap = prototypeWorld.getEntitiesMap();
        Map<String, Integer> userInputMap = userInput.getEntityPopulationMap();
        //
        for (EntitiesDefinition prototype : prototypeEntitiesDefinitionMap.values()) {
            String entityName = prototype.getEntityName();
            int population;
            if (userInputMap.get(entityName) != null) {
                population = userInputMap.get(entityName);
            } else population = 0;

            EntitiesDefinition entitiesDefinition = new EntitiesDefinition(entityName, population);

            // get the prototype instance from the definition:
            EntityInstance prototypeInstance = prototypeWorld.getEntitiesDefinition(entityName).getPrototypeEntity();

            entitiesDefinition.setPrototypeEntity(prototypeInstance);

            for (int i = 0; i < population; i++) {
                EntityInstance newInstance = entityGenerator.generateNewInstance(prototypeInstance, newWorld.getGrid(), i);
                entitiesDefinition.addEntity(newInstance, i);
            }
            entitiesDefinitionMap.put(entityName, entitiesDefinition);
        }
        return entitiesDefinitionMap;
    }
}

