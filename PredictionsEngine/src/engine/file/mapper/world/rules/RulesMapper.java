package engine.file.mapper.world.rules;

import engine.file.mapper.world.rules.rule.RuleMapper;
import scheme.v1.generated.PRDRule;
import scheme.v1.generated.PRDRules;
import world.entities.EntitiesDefinition;
import world.rules.Rules;
import world.rules.rule.api.Rule;

public class RulesMapper {
    public static Rules mapRules(PRDRules jaxbRules, EntitiesDefinition entitiesContext) {
        Rules rules = new Rules();
        for (PRDRule jaxbRule: jaxbRules.getPRDRule()){
            Rule rule = RuleMapper.mapRule(jaxbRule ,entitiesContext);
            rules.addRule(rule);
        }
        return rules;
    }
}
