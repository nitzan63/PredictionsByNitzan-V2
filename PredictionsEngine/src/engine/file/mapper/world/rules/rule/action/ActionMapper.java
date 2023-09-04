package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import scheme.generated.PRDElse;
import scheme.generated.PRDThen;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.impl.KillAction;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionMapper {
    public static Action mapAction (PRDAction jaxbAction, Map<String, EntitiesDefinition> entitiesContext){
        String actionType = jaxbAction.getType().toLowerCase();
        String entityName = jaxbAction.getEntity();
        SecondaryEntity secondaryEntity = null;
        if (jaxbAction.getPRDSecondaryEntity() != null) {
            secondaryEntity = SecondaryEntityMapper.mapSecondaryEntity(jaxbAction.getPRDSecondaryEntity(), entitiesContext);
        }
        switch (actionType){
            case "increase":
                return IncreaseActionMapper.mapIncreaseAction(jaxbAction, secondaryEntity);
            case "decrease":
                return DecreaseActionMapper.mapDecreaseAction(jaxbAction, secondaryEntity);
            case "calculation":
                return CalculationActionMapper.mapCalculationAction(jaxbAction, secondaryEntity);
            case "kill":
                return new KillAction(entityName, secondaryEntity);
            case "set":
                return SetActionMapper.mapSetAction(jaxbAction, secondaryEntity);
            case "condition":
                return ConditionActionMapper.mapConditionAction(jaxbAction, entitiesContext, secondaryEntity);
            case "proximity":
                return ProximityActionMapper.mapProximityAction(jaxbAction, entitiesContext, secondaryEntity);
            case "replace":
                return ReplaceActionMapper.mapReplaceAction(jaxbAction, secondaryEntity);
            default:
                return null;
        }
    }
    public static List<Action> mapActions(PRDThen jaxbThen, Map<String, EntitiesDefinition> entitiesContext){
        List<Action> actions = new ArrayList<>();
        for (PRDAction jaxbAction : jaxbThen.getPRDAction()){
            Action action = mapAction(jaxbAction, entitiesContext);
            actions.add(action);
        }
        return actions;
    }

    public static List<Action> mapActions(PRDElse prdElse, Map<String, EntitiesDefinition> entitiesContext){
        List<Action> actions = new ArrayList<>();
        for (PRDAction jaxbAction : prdElse.getPRDAction()){
            Action action = mapAction(jaxbAction, entitiesContext);
            actions.add(action);
        }
        return actions;
    }
}
