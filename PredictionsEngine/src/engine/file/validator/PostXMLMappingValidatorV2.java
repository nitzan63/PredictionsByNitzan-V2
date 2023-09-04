package engine.file.validator;

import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.rules.Rules;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionContext;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.impl.ConditionAction;
import world.rules.rule.action.impl.ProximityAction;
import world.rules.rule.api.Rule;
import world.utils.expression.ExpressionEvaluator;

import javax.xml.bind.ValidationException;
import java.util.Map;

public class PostXMLMappingValidatorV2 {
    // Main validation entry point
    public static void validateXMLPostMapping(World world) throws ValidationException {
        // Preliminary checks on the World object
        validateWorldState(world);

        Map<String, EntitiesDefinition> entitiesDefinitionMap = world.getEntitiesMap();
        Rules rules = world.getRules();

        ActionContext actionContext = new ActionContext(world, 0);

        // Iterate through each rule for validation
        for (Rule rule : rules.getRules()) {
            validateRule(rule, entitiesDefinitionMap, actionContext);
        }
    }

    // Validate the initial state of the World object
    private static void validateWorldState(World world) throws ValidationException {
        
        if (world == null) {
            throw new ValidationException("World object is null.");
        }

        // Validate environment
        if (world.getEnvironment() == null) {
            throw new ValidationException("Environment in world is not set.");
        }

        // Validate rules
        if (world.getRules() == null) {
            throw new ValidationException("Rules in world are not set.");
        }

        // Validate grid
        if (world.getGrid() == null) {
            throw new ValidationException("Grid in world is not set.");
        }

        // Validate termination
        if (world.getTermination() == null) {
            throw new ValidationException("Termination in world is not set.");
        }

        // Validate entitiesMap
        if (world.getEntitiesMap() == null || world.getEntitiesMap().isEmpty()) {
            throw new ValidationException("Entities in world are not set or empty.");
        }
    }

    // Validate a single rule
    private static void validateRule(Rule rule, Map<String, EntitiesDefinition> entitiesDefinitionMap, ActionContext actionContext) throws ValidationException {
        for (Action action : rule.getActionsToPerform()) {
            validateAction(action, entitiesDefinitionMap, actionContext);
        }
    }

    // Validate a single action
    private static void validateAction(Action action, Map<String, EntitiesDefinition> entitiesDefinitionMap, ActionContext actionContext) throws ValidationException {
        if (action instanceof ConditionAction) {
            validateConditionAction((ConditionAction) action, entitiesDefinitionMap, actionContext);
        } else {
            validateStandardAction(action, entitiesDefinitionMap, actionContext);
        }
    }

    // Validate a ConditionAction
    private static void validateConditionAction(ConditionAction conditionAction, Map<String, EntitiesDefinition> entitiesDefinitionMap, ActionContext actionContext) throws ValidationException {
        for (Action thenAction : conditionAction.getThenActions()) {
            validateAction(thenAction, entitiesDefinitionMap, actionContext);
        }
        if (conditionAction.getElseActions() != null) {
            for (Action elseAction : conditionAction.getElseActions()) {
                validateAction(elseAction, entitiesDefinitionMap, actionContext);
            }
        }
    }

    // Validate a standard (non-ConditionAction) action
    private static void validateStandardAction(Action action, Map<String, EntitiesDefinition> entitiesDefinitionMap, ActionContext actionContext) throws ValidationException {
        validateEntityName(action, entitiesDefinitionMap);
        validatePropertyName(action, entitiesDefinitionMap);
        EntityInstance exampleEntity = entitiesDefinitionMap.get(action.getEntityName()).getPrototypeEntity();
        validateByExpression(action, exampleEntity, actionContext);
    }

    // Validate the entity name in an action
    private static void validateEntityName(Action action, Map<String, EntitiesDefinition> entitiesDefinitionMap) throws ValidationException {
        String contextEntityName = action.getEntityName();
        if (entitiesDefinitionMap.get(contextEntityName) == null) {
            throw new ValidationException("Entity name: " + contextEntityName + " does not exist!\n");
        }
    }

    // Validate the property name in an action
    private static void validatePropertyName(Action action, Map<String, EntitiesDefinition> entitiesDefinitionMap) throws ValidationException {
        if (action.getPropertyName() != null) {
            String contextPropName = action.getPropertyName();
            EntityInstance exampleEntity = entitiesDefinitionMap.get(action.getEntityName()).getPrototypeEntity();
            if (!doesPropertyExist(contextPropName, exampleEntity)) {
                throw new ValidationException("Property Name: " + contextPropName + " does not exist!\n");
            }
        }
    }

    // Check if a property exists in the given entity instance
    private static boolean doesPropertyExist(String propertyName, EntityInstance entityInstance) {
        for (EntityProperty prop : entityInstance.getProperties().getProperties()) {
            if (prop.getName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    // Validate the byExpression in an action
    private static void validateByExpression(Action action, EntityInstance exampleEntity, ActionContext actionContext) throws ValidationException {
        if (action.getByExpression() != null && (action.getActionType() == ActionType.DECREASE || action.getActionType() == ActionType.INCREASE)) {
            Object evaluatedValue = ExpressionEvaluator.evaluateExpression(action.getByExpression(), exampleEntity, actionContext);
            if (!(evaluatedValue instanceof Number)) {
                throw new ValidationException("byExpression value: " + action.getByExpression() + " is not a number!\n");
            }
        }

        if (action instanceof ProximityAction) {
            ProximityAction proximityAction = (ProximityAction) action;
            Object evaluatedValue = ExpressionEvaluator.evaluateExpression(proximityAction.getByExpression(), exampleEntity, actionContext);
            if (!(evaluatedValue instanceof Number)) {
                throw new ValidationException("ofExpression in proximity value: " + action.getByExpression() + " is not a number!\n");
            }
        }
    }
}
