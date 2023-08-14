package world.rules.rule.action.condition.impl;

import world.entities.entity.EntityInstance;
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
                this.logicalOperator = null; //TODO handle this case. make sure to pass the logical operator as lowercase.
        }
    }

    public void addCondition(Condition condition) {
        conditionList.add(condition);
    }

    @Override
    public boolean evaluate(EntityInstance entityInstance) {
        if (logicalOperator != null) {
            switch (logicalOperator) {
                case AND:
                    return conditionList.stream().allMatch(condition -> condition.evaluate(entityInstance));
                case OR:
                    return conditionList.stream().anyMatch(condition -> condition.evaluate(entityInstance));
                default:
                    return false; //TODO handle this case if needed.
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
