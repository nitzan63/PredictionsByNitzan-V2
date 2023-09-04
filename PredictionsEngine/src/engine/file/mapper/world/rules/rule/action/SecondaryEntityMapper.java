package engine.file.mapper.world.rules.rule.action;

import engine.file.mapper.world.rules.rule.condition.ConditionMapper;
import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class SecondaryEntityMapper {
    public static SecondaryEntity mapSecondaryEntity(PRDAction.PRDSecondaryEntity jaxbSecondary, Map<String, EntitiesDefinition> entitiesMap){
        String count = jaxbSecondary.getPRDSelection().getCount();
        Condition condition = ConditionMapper.mapCondition(jaxbSecondary.getPRDSelection().getPRDCondition());
        return new SecondaryEntity(count, condition, jaxbSecondary.getEntity());
    }
}
