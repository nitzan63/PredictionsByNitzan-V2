package engine.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.CalculationAction;
import world.rules.rule.action.impl.KillAction;

public class ActionMapper {
    public static Action mapAction (PRDAction jaxbAction, EntitiesDefinition entitiesContext){
        String actionType = jaxbAction.getType().toLowerCase();
        switch (actionType){
            case "increase":
                return IncreaseActionMapper.mapIncreaseAction(jaxbAction, entitiesContext);
            case "decrease":
                return DecreaseActionMapper.mapDecreaseAction(jaxbAction, entitiesContext);
            case "calculation":
                return CalculationActionMapper.mapCalculationAction(jaxbAction, entitiesContext);
            case "kill":
                return new KillAction(entitiesContext);
            case "set":
                return SetActionMapper.mapSetAction(jaxbAction, entitiesContext);
            case "condition":
                //return ConditionActionMapper.mapConditionAction(jaxbAction, entitiesContext);
            default:
                return null; //TODO handle this case.
        }
    }
}
