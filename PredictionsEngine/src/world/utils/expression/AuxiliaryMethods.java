package world.utils.expression;

import world.entities.entity.EntityInstance;
import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.action.api.ActionContext;

import java.util.Arrays;
import java.util.Random;

public class AuxiliaryMethods {
    public static Double environmentAuxMethod(String propertyName, Environment environment){
        Object value = environment.getEnvironmentPropValue(propertyName);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException(propertyName + " is not a number in the environment.");
        }
    }

    public static Double randomAuxMethod (String args){
        int number = Integer.parseInt(args);
        return new Random().nextDouble()*(number);
    }

    public static Object evaluateAuxMethod (String expression, EntityInstance entityInstance, ActionContext actionContext){
        // split the expression:
        String[] parts = expression.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("The expression format should be <entity>.<property>");
        }
        // get entity name and property name:
        String entityName = parts[0];
        String propertyName = parts[1];

        EntityInstance secondaryInstance = actionContext.getSecondaryEntityInContext();
        EntityProperty property;

        // check which instance is relevant:
        if (secondaryInstance != null){
            // if the second entity in context is relevant:
            if (secondaryInstance.getEntityName().equals(entityName)){
                // check if there is a relevant property, and return:
                property = secondaryInstance.getProperty(propertyName);
                if (property != null)
                    return property.getValue();
                else throw new IllegalArgumentException("The entity: " + secondaryInstance.getEntityName() +" has no property: " + propertyName);
            }
        }
        if (entityInstance.getEntityName().equals(entityName)){
            property = entityInstance.getProperty(propertyName);
            if (property != null)
                return property.getValue();
            else throw new IllegalArgumentException("The entity " + entityInstance.getEntityName() + " has no property: " + propertyName);
        }
        throw new IllegalArgumentException("There is no entity " + entityName + " in the context of the action");
    }

    public static Number percentAuxMethod(String argument, EntityInstance entityInstance, ActionContext actionContext){

        String args [] = argument.split(",");
        // add a check to a valid split
        if (args.length != 2)
            throw new IllegalArgumentException("Illegal argument in percent function: " + argument);

        Object wholeObj = ExpressionEvaluator.evaluateExpression(args[0], entityInstance, actionContext);
        Object percentObj = ExpressionEvaluator.evaluateExpression(args[1], entityInstance, actionContext);

        if (wholeObj instanceof Number && percentObj instanceof Number){
            Double whole = ((Number) wholeObj).doubleValue();
            Double percent = ((Number) percentObj).doubleValue();

            return (whole * percent) / 100.0;
        }

        else throw new IllegalArgumentException("Arguments must be numbers: " + Arrays.toString(args));

    }
}
