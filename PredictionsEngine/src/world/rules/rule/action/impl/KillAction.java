package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.grid.Grid;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

import java.util.Map;

public class KillAction extends AbstractAction {
    public KillAction(String entityName) {
        super(ActionType.KILL, entityName);
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        // get objects from context:
        Grid grid = actionContext.getGrid();
        Map<String, EntitiesDefinition> allEntitiesDefinitionMap = actionContext.getEntitiesMap();

        // get the relevant entities definition
        EntitiesDefinition entityDefinitionToKill = allEntitiesDefinitionMap.get(entityName);

        // remove entity instance from instances:
        entityDefinitionToKill.removeEntityInstance(entityInstance.getSerialNumber());
        entityDefinitionToKill.setPopulation(entityDefinitionToKill.getPopulation() - 1);

        // remove from grid:
        grid.removeEntityFromGrid(entityInstance);

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
