package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.grid.Grid;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class KillAction extends AbstractAction {
    public KillAction(String entityName, SecondaryEntity secondaryEntity) {
        super(ActionType.KILL, entityName, secondaryEntity);
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        // check if there is a secondary entity:
        if (secondaryEntity != null){
            // if yes, check if the intended action is in the context of the secondary entity:
            if (secondaryEntity.getDefinitionEntityName().equals(entityName)){
                // if yes, get the map of the selected entities:
                Map<Integer, EntityInstance> secondaryEntities = secondaryEntity.getSelectedSecondaryInstancesMap(actionContext);
                // iterate over them and perform actions
                for (EntityInstance secondaryEntity : secondaryEntities.values()){
                    performAction(secondaryEntity, actionContext);
                }
                // if there is a secondary entity, but the action is performed on the primary entity:
            } else performAction(entityInstance, actionContext);
            // if there is no secondaryEntity, perform on the main entity.
        } else performAction(entityInstance, actionContext);
    }

    private void performAction (EntityInstance entityInstance, ActionContext actionContext){
        // get objects from context:
        Grid grid = actionContext.getGrid();
        Map<String, EntitiesDefinition> allEntitiesDefinitionMap = actionContext.getEntitiesMap();

        // get the relevant entities definition
        EntitiesDefinition entityDefinitionToKill = allEntitiesDefinitionMap.get(entityName);

        // remove entity instance from instances:
        entityDefinitionToKill.removeEntityInstance(entityInstance.getSerialNumber(), actionContext.getTick());

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
