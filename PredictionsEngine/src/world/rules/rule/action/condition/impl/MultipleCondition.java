package world.rules.rule.action.condition.impl;

import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.ActionContext;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.condition.api.LogicalOperator;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition implements Condition {
    private final List<Condition> conditionList;
    private final LogicalOperator logicalOperator;

    public MultipleCondition(String logicalOperator) {
        this.conditionList = new ArrayList<>();
        switch (logicalOperator) {
            case "and":
                this.logicalOperator = LogicalOperator.AND;
                break;
            case "or":
                this.logicalOperator = LogicalOperator.OR;
                break;
            default:
                this.logicalOperator = null;
        }
    }

    public void addCondition(Condition condition) {
        conditionList.add(condition);
    }

    @Override
    public boolean evaluate(EntityInstance entityInstance, EntityInstance secondaryInstance, String secondaryName, ActionContext actionContext) {
        if (logicalOperator != null) {
            switch (logicalOperator) {
                case AND:
                    return conditionList.stream().allMatch(condition -> condition.evaluate(entityInstance, secondaryInstance, secondaryName, actionContext));
                case OR:
                    return conditionList.stream().anyMatch(condition -> condition.evaluate(entityInstance, secondaryInstance, secondaryName, actionContext));
                default:
                    return false;
            }
        } else return false;

    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "conditionList=" + conditionList +
                ", logicalOperator=" + logicalOperator +
                '}';
    }
}
