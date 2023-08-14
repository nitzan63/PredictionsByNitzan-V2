package world.utils.expression;

import world.entities.entity.EntityInstance;

public class ExpressionEvaluator {
    public static Object evaluateExpression (String expression, EntityInstance entityInstance){
        // check if the expression is in a format of auxiliary method:
        if (expression.matches("\\w+\\(.+\\)")){
            String[] parts = expression.split("\\(");
            String methodName = parts[0];
            String argument = parts[1].substring(0, parts[1].length() - 1);
            return evaluateAuxMethod(methodName, argument);
        } else if (entityInstance.getProperty(expression) != null){ //check if the expression is a property
            return entityInstance.getProperty(expression).getValue();
        } else return parseFreeValue(expression); // the expression is free value, parse it.
    }

    private static Object evaluateAuxMethod (String methodName, String argument){
        switch (methodName){
            case "environment":
                return AuxiliaryMethods.environmentAuxMethod(argument);
            case "random":
                return AuxiliaryMethods.randomAuxMethod(argument);
            default:
                throw new IllegalArgumentException("Unknown Auxiliary Method!");
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
