package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.impl.ProximityAction;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProximityActionMapper {
    public static ProximityAction mapProximityAction(PRDAction jaxbAction, Map<String, EntitiesDefinition> entitiesMap, SecondaryEntity secondaryEntity){
        // get strings:
        String sourceEntity = jaxbAction.getPRDBetween().getSourceEntity();
        String targetEntity = jaxbAction.getPRDBetween().getTargetEntity();
        String depth = jaxbAction.getPRDEnvDepth().getOf();

        // build actions to perform list:
        List<PRDAction> jaxbActionList = jaxbAction.getPRDActions().getPRDAction();
        List<Action> actionList = new ArrayList<>();
        for (PRDAction inAction : jaxbActionList){
            Action action = ActionMapper.mapAction(inAction, entitiesMap);
            actionList.add(action);
        }
        // build new proximity action:
        return new ProximityAction(sourceEntity, targetEntity, depth, actionList, secondaryEntity);
    }
}
