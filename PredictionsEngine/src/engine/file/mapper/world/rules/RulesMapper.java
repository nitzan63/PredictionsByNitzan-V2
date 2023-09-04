package engine.file.mapper.world.rules;

import engine.file.mapper.world.rules.rule.RuleMapper;
import scheme.generated.PRDRule;
import scheme.generated.PRDRules;
import world.entities.EntitiesDefinition;
import world.rules.Rules;
import world.rules.rule.api.Rule;

import java.util.Map;

public class RulesMapper {
    public static Rules mapRules(PRDRules jaxbRules, Map<String, EntitiesDefinition> entitiesContext) {
        Rules rules = new Rules();
        for (PRDRule jaxbRule: jaxbRules.getPRDRule()){
            Rule rule = RuleMapper.mapRule(jaxbRule ,entitiesContext);
            rules.addRule(rule);
        }
        return rules;
    }
}
