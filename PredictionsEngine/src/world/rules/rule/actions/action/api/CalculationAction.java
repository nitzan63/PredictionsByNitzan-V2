package world.rules.rule.actions.action.api;

import world.entities.EntitiesDefinition;
import world.rules.rule.actions.action.api.AbstractAction;
import world.rules.rule.actions.action.api.ActionType;

public abstract class CalculationAction extends AbstractAction {
    protected final String entityName;
    protected final String resProp;
    protected final String args1;
    protected final String args2;

    public CalculationAction(EntitiesDefinition entitiesDefinition, String entityName, String resultProp, String s1, String s2){
        super(ActionType.CALCULATION , entitiesDefinition);
        this.entityName = entityName;
        this.resProp = resultProp;
        this.args1 = s1;
        this.args2 = s2;
    }

    public String getEntityName() {
        return entityName;
    }


    public String getPropertyName() {
        return resProp;
    }
}
