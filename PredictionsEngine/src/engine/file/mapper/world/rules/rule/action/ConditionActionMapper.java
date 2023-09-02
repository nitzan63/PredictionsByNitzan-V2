package engine.file.mapper.world.rules.rule.action;

import engine.file.mapper.world.rules.rule.condition.ConditionMapper;
import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.impl.ConditionAction;

import java.util.List;

public class ConditionActionMapper {
    public static ConditionAction mapConditionAction(PRDAction jaxbAction, EntitiesDefinition entitiesContext){
        String entityName = jaxbAction.getEntity();
        Condition condition = ConditionMapper.mapCondition(jaxbAction.getPRDCondition(), entitiesContext);
        List<Action> thenAction = ActionMapper.mapActions(jaxbAction.getPRDThen(), entitiesContext);
        if (jaxbAction.getPRDElse() != null) { // if there is an "else" section:
            List<Action> elseAction = ActionMapper.mapActions(jaxbAction.getPRDElse(), entitiesContext);
            return new ConditionAction(entitiesContext, entityName, thenAction, elseAction, condition);
        } else { // else - there is no else section, so sends null
            return new ConditionAction(entitiesContext, entityName, thenAction, null, condition);
        }
    }
}
