package world.rules.rule.action.secondary;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.ActionContext;
import world.rules.rule.action.condition.api.Condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SecondaryEntity {
    private final String count;
    private final Condition condition;
    private final String entityName;

    public SecondaryEntity(String count, Condition condition, String entityName) {
        this.count = count;
        this.condition = condition;
        this.entityName = entityName;
    }

    public Map<Integer, EntityInstance> getSelectedSecondaryInstancesMap(ActionContext actionContext) {

        EntitiesDefinition entitiesDefinition = actionContext.getEntitiesMap().get(entityName);

        // if count is all - return all the entities that fit the conditions:
        if (count.equals("ALL"))
            return buildEntitiesMapByConditions(entitiesDefinition, actionContext);

        // get the population and parse the count to number:
        Integer numOfInstances = Integer.parseInt(count);
        Integer population = entitiesDefinition.getPopulation();

        // if there are more to select than the population, return all the entities that fit the conditions
        if (population <= numOfInstances)
            return buildEntitiesMapByConditions(entitiesDefinition, actionContext);

        // get the random number of fit entities:
        int addedEntities = 0;
        Map <Integer, EntityInstance> resMap = new HashMap<>();
        Map <Integer, EntityInstance> secondaryEntities = entitiesDefinition.getEntities();
        Random random = new Random();

        int attempts = 0;


        while (addedEntities < numOfInstances){
            if (attempts > 10 * population)
                break;
            // select a random entity:
            Integer selectedInstance = random.nextInt(population);
            EntityInstance instance = secondaryEntities.get(selectedInstance);
            if (instance != null){
                // check if the instance fits the conditions:
                if (isInstanceFitConditions(instance, actionContext)) {
                    resMap.put(selectedInstance, instance);
                    addedEntities ++;
                }
            }
            attempts ++;
        }

        return resMap;

    }

    private Map<Integer, EntityInstance> buildEntitiesMapByConditions(EntitiesDefinition entitiesDefinition, ActionContext actionContext){

        if (condition == null)
            return  entitiesDefinition.getEntities();

        Map<Integer, EntityInstance> resMap = new HashMap<>();
        Map<Integer, EntityInstance> secondaryEntities = entitiesDefinition.getEntities();

        for (EntityInstance instance : secondaryEntities.values()) {
            if (isInstanceFitConditions(instance, actionContext)) {
                resMap.put(instance.getSerialNumber(), instance);
            }
        }

        return resMap;
    }

    private boolean isInstanceFitConditions (EntityInstance instance, ActionContext actionContext){
        if (condition == null)
            return true;
        return condition.evaluate(instance,null, null, actionContext);
    }

    public String getDefinitionEntityName() {
        return entityName;
    }
}
