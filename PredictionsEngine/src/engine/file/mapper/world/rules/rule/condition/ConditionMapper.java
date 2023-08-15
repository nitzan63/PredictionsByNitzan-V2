package engine.file.mapper.world.rules.rule.condition;

import scheme.generated.PRDCondition;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.condition.api.Condition;
import world.rules.rule.action.condition.impl.MultipleCondition;
import world.rules.rule.action.condition.impl.SingleCondition;

import java.util.List;

public class ConditionMapper {
    public static Condition mapCondition (PRDCondition jaxbCondition, EntitiesDefinition entitiesContext){
        String singularity = jaxbCondition.getSingularity().toLowerCase();

        if ("single".equalsIgnoreCase(singularity)){
            return new SingleCondition(jaxbCondition.getProperty(), jaxbCondition.getOperator(), jaxbCondition.getValue());
        } else {
            MultipleCondition multipleCondition = new MultipleCondition(jaxbCondition.getLogical());
            List<PRDCondition> subConditions = jaxbCondition.getPRDCondition(); // Assuming PRDCondition contains a list called subConditions when singularity is not single.
            for (PRDCondition subCondition : subConditions) {
                Condition mappedSubCondition = mapCondition(subCondition, entitiesContext);
                multipleCondition.addCondition(mappedSubCondition);
            }
            return multipleCondition;
        }
    }
}
