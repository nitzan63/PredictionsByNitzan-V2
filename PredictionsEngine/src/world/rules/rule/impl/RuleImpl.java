package world.rules.rule.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;
import world.rules.rule.api.Rule;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public void performActions(EntitiesDefinition entitiesDefinition, int tickNumber) throws Exception {
        try {
            if (activation.isActive(tickNumber)) {
                Collection<EntityInstance> entitiesCopy = new ArrayList<>(entitiesDefinition.getEntities().values());
                for (EntityInstance entity : entitiesCopy) {
                    for (Action action : actionsToPerform) {
                        action.invoke(entity);
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
