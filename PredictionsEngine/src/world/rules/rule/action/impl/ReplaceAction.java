package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.generator.EntityGenerator;
import world.grid.Grid;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class ReplaceAction extends AbstractAction{
    private final String killEntityName;
    private final String createEntityName;
    private final String mode;


    public ReplaceAction(String killEntityName, String createEntityName, String mode , SecondaryEntity secondaryEntity) {
        super(ActionType.REPLACE, killEntityName, secondaryEntity);
        this.killEntityName = killEntityName;
        this.createEntityName = createEntityName;
        this.mode = mode;
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
        // get the data from context:
        Map<String, EntitiesDefinition> allEntitiesDefinitionMap = actionContext.getEntitiesMap();
        Grid grid = actionContext.getGrid();

        // get relevant entities:
        EntitiesDefinition entityDefinitionToKill = allEntitiesDefinitionMap.get(killEntityName);
        EntitiesDefinition entitiesDefinitionToCreate = allEntitiesDefinitionMap.get(createEntityName);

        // Step 1: Kill the target entity and remove from grid
        entityDefinitionToKill.removeEntityInstance(entityInstance.getSerialNumber(), actionContext.getTick());
        grid.removeEntityFromGrid(entityInstance);

        EntityGenerator entityGenerator = new EntityGenerator();

        // Step 2: Create a new entity:
        EntityInstance newInstance;
        if ("scratch".equals(mode)){
            newInstance = entityGenerator.replaceFromScratch(entitiesDefinitionToCreate.getPrototypeEntity(), entityInstance);
        } else if ("derived".equals(mode)){
            newInstance = entityGenerator.replaceDerived(entitiesDefinitionToCreate.getPrototypeEntity(), entityInstance);
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }


        // find available serial number:
        int newSerialNumber = 0;
        while (entitiesDefinitionToCreate.getEntity(newSerialNumber) != null){
            newSerialNumber ++;
        } // set new serial number:
        newInstance.setSerialNumber(newSerialNumber);

        // Step 3: Add the new entity instance to the entities and add to the grid:
        entitiesDefinitionToCreate.addEntity(newInstance, newInstance.getSerialNumber());
        entitiesDefinitionToCreate.setPopulation(entitiesDefinitionToCreate.getPopulation() + 1);
        grid.addEntityToGrid(newInstance);


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
