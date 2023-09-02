package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

import java.util.Map;

public class KillAction extends AbstractAction {
    public KillAction(Map<String,EntitiesDefinition> allEntitiesDefinition, String entityName) {
        super(ActionType.KILL, allEntitiesDefinition, entityName);
    }

    @Override
    public void invoke(EntityInstance entityInstance, Environment environment) {
        EntitiesDefinition entityDefinitionToKill = allEntitiesDefinitionMap.get(entityName);
        entityDefinitionToKill.removeEntityInstance(entityInstance.getSerialNumber());
        entityDefinitionToKill.setPopulation(entityDefinitionToKill.getPopulation() - 1);
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public String getByExpression() {
        return null;
    }


}
