package engine.file.validator;

import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.rules.Rules;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.api.CalculationAction;
import world.rules.rule.action.impl.ConditionAction;
import world.rules.rule.api.Rule;
import world.utils.expression.ExpressionEvaluator;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class PostXMLMappingValidator {
    public static void validateXMLPostMapping(World world) throws ValidationException {
        String actualEntitiesName = world.getEntities().getEntityName();
        EntityInstance exampleEntity = world.getEntities().getEntity(1);
        Rules rules = world.getRules();

        // For each rule:
        for (Rule rule : rules.getRules()) {
            // For each action:
            for (Action action : rule.getActionsToPerform()) {
                validateAction(action, rule, actualEntitiesName, exampleEntity, world);
            }
        }
    }

            private static List<String> getPropertyNames (World world){
                List<String> propNames = new ArrayList<>();
                for (EntityProperty prop : world.getEntities().getEntity(1).getProperties().getProperties()) {
                    propNames.add(prop.getName());
                }
                return propNames;
            }

    private static void validateAction(Action action, Rule rule, String actualEntitiesName, EntityInstance exampleEntity, World world) throws ValidationException {
        if (action instanceof ConditionAction) {
            ConditionAction conditionAction = (ConditionAction) action;
            for (Action thenAction : conditionAction.getThenActions()) {
                validateAction(thenAction, rule, actualEntitiesName, exampleEntity, world);
            }
            if (conditionAction.getElseActions() != null) {
                for (Action elseAction : conditionAction.getElseActions()) {
                    validateAction(elseAction, rule, actualEntitiesName, exampleEntity, world);
                }
            }
        } else {
            // check that the name exists:
            String contextEntityName = action.getEntityName();
            if (!contextEntityName.equals(actualEntitiesName)) {
                throw new ValidationException("Entity name: " + contextEntityName + ", in rule " + rule.getName() + " does not exist!\n");
            }
            // check property name exists:
            if (action.getPropertyName() != null) {
                String contextPropName = action.getPropertyName();
                List<String> propertiesNames = getPropertyNames(exampleEntity);
                boolean propertyExists = false;
                for (String actualPropName : propertiesNames) {
                    if (actualPropName.equals(contextPropName)) {
                        propertyExists = true;
                        break;
                    }
                }
                if (!propertyExists) {
                    throw new ValidationException("Property Name: " + contextPropName + ", in rule " + rule.getName() + " does not exist!\n");
                }
            }
            // check if the byExpression is valid:
            if (action.getByExpression() != null && ((action.getActionType() == ActionType.DECREASE) || (action.getActionType() == ActionType.INCREASE))) {
                // check for decrease / increase:
                Object evaluatedValue = ExpressionEvaluator.evaluateExpression(action.getByExpression(), exampleEntity, world.getEnvironment());
                if (!(evaluatedValue instanceof Number)) {
                    throw new ValidationException("byExpression value: " + action.getByExpression() + ", in rule " + rule.getName() + " is not a number!\n");
                }
            }
            else if (action.getActionType() == ActionType.CALCULATION) {
                if (action instanceof CalculationAction) {
                    //check for calculation args:
                    CalculationAction calcAction = (CalculationAction) action;
                    Object evaluatedArg1 = ExpressionEvaluator.evaluateExpression(calcAction.getArgs1(), exampleEntity, world.getEnvironment());
                    Object evaluatedArg2 = ExpressionEvaluator.evaluateExpression(calcAction.getArgs2(), exampleEntity, world.getEnvironment());
                    if (!(evaluatedArg1 instanceof Number)) {
                        throw new ValidationException("args1 value: " + calcAction.getArgs1() + ", in calculation action in rule " + rule.getName() + " is not a number!\n");
                    }
                    if (!(evaluatedArg2 instanceof Number)) {
                        throw new ValidationException("args2 value: " + calcAction.getArgs2() + ", in calculation action in rule " + rule.getName() + " is not a number!\n");
                    }
                }
            }
        }
    }

    private static List<String> getPropertyNames(EntityInstance entityInstance) {
        List<String> propNames = new ArrayList<>();
        for (EntityProperty prop : entityInstance.getProperties().getProperties()) {
            propNames.add(prop.getName());
        }
        return propNames;
    }
}