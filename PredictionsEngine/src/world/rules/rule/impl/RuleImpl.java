package world.rules.rule.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;
import world.rules.rule.api.Rule;

import java.util.*;

public class RuleImpl implements Rule {
    private final String name;
    private Activation activation;
    private final List<Action> actionsToPerform;


    public RuleImpl(String name, Activation activation) {
        this.name = name;
        this.actionsToPerform = new ArrayList<>();
        this.activation = activation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Activation getActivation() {
        return activation;
    }

    @Override
    public List<Action> getActionsToPerform() {
        return actionsToPerform;
    }

    @Override
    public void addAction(Action action) {
        actionsToPerform.add(action);
    }

    public void performActions(int tickNumber, ActionContext actionContext) throws Exception {
        try {
            // Check if rule is active:
            if (activation.isActive(tickNumber)) {
                Map<String, EntitiesDefinition> entitiesMap = actionContext.getEntitiesMap();
                // Iterate over all entity types:
                for (Map.Entry<String, EntitiesDefinition> entry : entitiesMap.entrySet()) {

                    String entityName = entry.getKey();
                    EntitiesDefinition entitiesDefinition = entry.getValue();

                    // Create a copy of the entity instances to run on (for kill action):
                    Collection<EntityInstance> entitiesCopy = new ArrayList<>(entitiesDefinition.getEntities().values());

                    // Iterate over all entity instances:
                    for (EntityInstance entity : entitiesCopy) {
                        for (Action action : actionsToPerform) {

                            // check if the action is related to this entity:
                            if (action.getEntityName().equals(entityName)) {
                                // invoke the action on the entity instance.
                                action.invoke(entity, actionContext);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new Exception ("Error in rule " + name + e.getMessage() , e);
        }
    }

    @Override
    public String toString() {
        return "RuleImpl{" +
                "name='" + name + '\'' +
                ", activation=" + activation +
                ", actionsToPerform=" + actionsToPerform +
                '}';
    }
}
