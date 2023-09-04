package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import scheme.generated.PRDDivide;
import scheme.generated.PRDMultiply;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.impl.DivideAction;
import world.rules.rule.action.impl.MultiplyAction;
import world.rules.rule.action.secondary.SecondaryEntity;

public class CalculationActionMapper {
    public static Action mapCalculationAction (PRDAction jaxbAction, SecondaryEntity secondaryEntity){
        String entityName = jaxbAction.getEntity();
        String resultProp = jaxbAction.getResultProp();
        if (jaxbAction.getPRDMultiply() != null){
            PRDMultiply multiply = jaxbAction.getPRDMultiply();
            return new MultiplyAction(entityName, resultProp, multiply.getArg1(), multiply.getArg2(), secondaryEntity);
        } else if (jaxbAction.getPRDDivide() != null){
            PRDDivide divide = jaxbAction.getPRDDivide();
            return new DivideAction(entityName, resultProp, divide.getArg1(), divide.getArg2(), secondaryEntity);
        }
        return null;
    }
}
