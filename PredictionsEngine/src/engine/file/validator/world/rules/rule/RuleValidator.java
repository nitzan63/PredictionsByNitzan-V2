package engine.file.validator.world.rules.rule;

import engine.file.validator.world.rules.rule.action.ActionValidator;
import engine.file.validator.world.rules.rule.activation.ActivationValidator;
import scheme.v1.generated.PRDAction;
import scheme.v1.generated.PRDRule;

import javax.xml.bind.ValidationException;

public class RuleValidator {
    public static void validateRule(PRDRule rule) throws ValidationException {
        if (rule.getName() == null || rule.getName().isEmpty()) {
            throw new ValidationException("Rule name is missing");
        }
        try {
            if (rule.getPRDActivation() != null)
                ActivationValidator.validateActivation(rule.getPRDActivation());
            for (PRDAction action : rule.getPRDActions().getPRDAction())
                ActionValidator.validateAction(action);
        } catch (ValidationException e) {
            throw new ValidationException("In rule " + rule.getName() + e.getMessage() , e);
        }
    }

}
