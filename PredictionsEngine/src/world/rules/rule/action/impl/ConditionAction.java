package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.condition.impl.MultipleCondition;
import world.rules.rule.action.condition.impl.SingleCondition;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.List;
import java.util.Map;

public class ConditionAction extends AbstractAction {
    private final Condition condition;
    private final List<Action> thenActions;
    private final List<Action> elseActions;

    public ConditionAction (String entityName, List<Action> thenActions, List<Action> elseActions, Condition condition, SecondaryEntity secondaryEntity){
        super(ActionType.CONDITION, entityName, secondaryEntity);
        this.elseActions = elseActions;
        this.thenActions = thenActions;
        this.condition = condition;
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {

        Map <Integer, EntityInstance> secondaryInstances = null;
        if (secondaryEntity != null){
            secondaryInstances = secondaryEntity.getSelectedSecondaryInstancesMap(actionContext);

            for (EntityInstance secondaryInstance : secondaryInstances.values()){
                if (condition.evaluate(entityInstance, secondaryInstance, secondaryEntity.getDefinitionEntityName(), actionContext)) {
                    performActions(thenActions, entityInstance, actionContext);
                } else if (elseActions != null){
                    performActions(elseActions, entityInstance, actionContext);
                }
            }
        } else {
            if (condition.evaluate(entityInstance,null, null, actionContext)) {
                performActions(thenActions, entityInstance, actionContext);
            } else if (elseActions != null){
                performActions(elseActions, entityInstance, actionContext);
            }
        }


    }


    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public String getByExpression() {
        return null;
    }

    private void performActions(List<Action> actions, EntityInstance entityInstance, ActionContext actionContext){
        for (Action action : actions)
            action.invoke(entityInstance, actionContext);
    }

    public List<Action> getThenActions() {
        return thenActions;
    }

    public List<Action> getElseActions() {
        return elseActions;
    }

    public int getNumberOfElseActions () {return elseActions.size(); }
    public int getNumberOfThenActions () {return thenActions.size(); }

    public String getMainConditionData () {
        StringBuilder sb = new StringBuilder();

        if (condition == null) {
            return "Condition is null";
        }

        if (condition instanceof SingleCondition) {
            SingleCondition singleCondition = (SingleCondition) condition;
            sb.append("Single Condition.");
            sb.append("\nProperty: ").append(singleCondition.getPropertyName());
            sb.append("\nOperator: ").append(singleCondition.getOperator().name());
            sb.append("\nValue: ").append(singleCondition.getRawValue());
        } else {
            MultipleCondition multipleCondition = (MultipleCondition) condition;
            sb.append("Multiple Condition.");
            sb.append("\nLogical Operator: ").append(multipleCondition.getLogicalOperator().name());
            sb.append("\nNumber of nested Conditions: ").append(multipleCondition.getNumberOfNestedConditions());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ConditionAction{" +
                "condition=" + condition +
                ", entityName='" + entityName + '\'' +
                ", thenActions=" + thenActions +
                ", elseActions=" + elseActions +
                '}';
    }
}
