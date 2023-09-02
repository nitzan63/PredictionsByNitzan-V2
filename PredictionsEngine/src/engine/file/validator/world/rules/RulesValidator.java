package engine.file.validator.world.rules;

import engine.file.validator.world.rules.rule.RuleValidator;
import scheme.generated.PRDRule;
import scheme.generated.PRDRules;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;

public class RulesValidator {
    public static void validateRules(PRDRules rules) throws ValidationException {
        if (rules == null || rules.getPRDRule().isEmpty()) {
            throw new ValidationException("Rules are empty.");
        }

        Set<String> ruleNames = new HashSet<>();

        for (PRDRule rule : rules.getPRDRule()) {
            RuleValidator.validateRule(rule);
            if (!ruleNames.add(rule.getName()))
                throw new ValidationException("Duplicate rule name:" + rule.getName());
        }
    }
}
