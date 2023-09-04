package world.rules.rule.action.condition.impl;

import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionContext;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.condition.api.Operator;
import world.utils.expression.ExpressionEvaluator;

import java.util.List;

public class SingleCondition implements Condition {
    private final String propertyName;
    private final Operator operator;
    private final String rawValue;
    private final String entityName;

    public SingleCondition(String property, String rawOperator, String rawValue, String entityName){
        this.rawValue = rawValue;
        this.propertyName = property;
        this.entityName = entityName;
        switch (rawOperator){
            case "=":
                this.operator = Operator.EQUAL;
                break;
            case "!=":
                this.operator = Operator.NOT_EQUAL;
                break;
            case "bt":
                this.operator = Operator.BT;
                break;
            case "lt":
                this.operator = Operator.LT;
                break;
            default:
                this.operator = null;
        }
    }


    public boolean evaluate(EntityInstance entityInstance, EntityInstance secondaryInstance, String secondaryName, ActionContext actionContext) {
        if (secondaryInstance != null){
            if (entityName.equals(secondaryName)){
                return performEvaluation(secondaryInstance, actionContext);
            } else return performEvaluation(entityInstance, actionContext);
        } else return performEvaluation(entityInstance, actionContext);

    }

    private boolean performEvaluation (EntityInstance entityInstance, ActionContext actionContext){
        Object propertyValue = entityInstance.getProperty(propertyName).getValue();
        Object value = ExpressionEvaluator.evaluateExpression(rawValue, entityInstance, actionContext);

        if (operator == null || value == null || propertyValue == null)
            return false;
        switch (operator){
            case EQUAL:
                return propertyValue.equals(value);
            case NOT_EQUAL:
                return (!propertyValue.equals(value));
            case BT:
                if (propertyValue instanceof Number && value instanceof Number){
                    Number propertyNum = (Number) propertyValue;
                    Number valueNum = (Number) value;
                    return propertyNum.doubleValue() > valueNum.doubleValue();
                }
                return false;
            case LT:
                if (propertyValue instanceof Number && value instanceof Number){
                    Number propertyNum = (Number) propertyValue;
                    Number valueNum = (Number) value;
                    return propertyNum.doubleValue() < valueNum.doubleValue();
                }
                return false;
            default:
                return false;
        }
    }
    @Override
    public String toString() {
        return "SingleCondition{" +
                "propertyName='" + propertyName + '\'' +
                ", operator=" + operator +
                ", rawValue='" + rawValue + '\'' +
                '}';
    }
}
