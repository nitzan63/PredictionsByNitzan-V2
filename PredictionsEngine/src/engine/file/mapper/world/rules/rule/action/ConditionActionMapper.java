package engine.file.mapper.world.rules.rule.action;

import engine.file.mapper.world.rules.rule.condition.ConditionMapper;
import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.impl.ConditionAction;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.List;
import java.util.Map;

public class ConditionActionMapper {
    public static ConditionAction mapConditionAction(PRDAction jaxbAction, Map<String, EntitiesDefinition> entitiesContext, SecondaryEntity secondaryEntity){
        String entityName = jaxbAction.getEntity();
        Condition condition = ConditionMapper.mapCondition(jaxbAction.getPRDCondition());
        List<Action> thenAction = ActionMapper.mapActions(jaxbAction.getPRDThen(), entitiesContext);
        if (jaxbAction.getPRDElse() != null) { // if there is an "else" section:
            List<Action> elseAction = ActionMapper.mapActions(jaxbAction.getPRDElse(), entitiesContext);
            return new ConditionAction(entityName, thenAction, elseAction, condition,secondaryEntity );
        } else { // else - there is no else section, so sends null
            return new ConditionAction(entityName, thenAction, null , condition, secondaryEntity);
        }
    }
}
