package world.utils.expression;

import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.ActionContext;

public class ExpressionEvaluator {
    public static Object evaluateExpression (String expression, EntityInstance entityInstance, ActionContext actionContext){
        // check if the expression is in a format of auxiliary method:
        if (expression.matches("^[a-zA-Z_][a-zA-Z_0-9]*\\((.*)\\)$")){
            String[] parts = expression.split("\\(", 2);
            String methodName = parts[0];
            String argument = parts[1].substring(0, parts[1].length() - 1);
            return evaluateAuxMethod(methodName, argument, actionContext, entityInstance);
        } else if (entityInstance.getProperty(expression) != null){ //check if the expression is a property
            return entityInstance.getProperty(expression).getValue();
        } else return parseFreeValue(expression); // the expression is free value, parse it.
    }

    private static Object evaluateAuxMethod (String methodName, String argument, ActionContext actionContext, EntityInstance entityInstance){
        switch (methodName){
            case "environment":
                return AuxiliaryMethods.environmentAuxMethod(argument, actionContext.getEnvironment());
            case "random":
                return AuxiliaryMethods.randomAuxMethod(argument);
            case "evaluate":
                return AuxiliaryMethods.evaluateAuxMethod(argument, entityInstance, actionContext);
            case "percent":
                return AuxiliaryMethods.percentAuxMethod(argument, entityInstance, actionContext);
            case "tick":
                return AuxiliaryMethods.tickAuxMethod(argument, entityInstance, actionContext);
            default:
                throw new IllegalArgumentException("Unknown Auxiliary Method: " + argument);
        }
    }

    private static Object parseFreeValue (String expression){
        if (expression.equals("true"))
            return true;
        else if (expression.equals("false"))
            return false;
        try {
            return Double.parseDouble(expression);
        } catch (NumberFormatException e){
            return expression;
        }
    }
}
