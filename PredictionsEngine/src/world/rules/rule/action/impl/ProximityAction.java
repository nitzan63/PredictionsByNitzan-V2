package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.grid.Grid;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.List;
import java.util.Map;

public class ProximityAction extends AbstractAction {
    private final String targetEntityName;
    private final String ofExpression;
    private final List<Action> actionsToPerform;

    public ProximityAction(String sourceEntityName, String targetEntityName, String ofExpression, List<Action> actionsToPerform, SecondaryEntity secondaryEntity) {
        super(ActionType.PROXIMITY, sourceEntityName, secondaryEntity);
        this.targetEntityName = targetEntityName;
        this.ofExpression = ofExpression;
        this.actionsToPerform = actionsToPerform;
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        // check if there is a secondary entity:
        if (secondaryEntity != null) {
            // if yes, check if the intended action is in the context of the secondary entity:
            if (secondaryEntity.getDefinitionEntityName().equals(entityName)) {
                // if yes, get the map of the selected entities:
                Map<Integer, EntityInstance> secondaryEntities = secondaryEntity.getSelectedSecondaryInstancesMap(actionContext);
                // iterate over them and perform actions
                for (EntityInstance secondaryEntity : secondaryEntities.values()) {
                    performAction(secondaryEntity, actionContext);
                }
                // if there is a secondary entity, but the action is performed on the primary entity:
            } else performAction(entityInstance, actionContext);
            // if there is no secondaryEntity, perform on the main entity.
        } else performAction(entityInstance, actionContext);
    }

    private void performAction(EntityInstance entityInstance, ActionContext actionContext) {

        // get relevant objects from context:
        Environment environment = actionContext.getEnvironment();
        Grid grid = actionContext.getGrid();
        EntitiesDefinition targetEntityDefinition = actionContext.getEntitiesMap().get(targetEntityName);

        // evaluate "of" expression:
        Double of = (Double) evaluateExpression(ofExpression, entityInstance, environment);

        // iterate over all target entities and check proximity:
        for (EntityInstance targetEntity : targetEntityDefinition.getEntities().values()) {
            if (isWithinProximity(entityInstance, targetEntity, grid, of)) {
                performActions(actionsToPerform, entityInstance, actionContext);
            }
        }
    }

    private boolean isWithinProximity(EntityInstance sourceEntity, EntityInstance targetEntity, Grid grid, Double proximity) {

        int maxRows = grid.getRows();
        int maxCols = grid.getCols();
        int sourceRow = sourceEntity.getRow();
        int sourceCol = sourceEntity.getCol();
        int targetRow = targetEntity.getRow();
        int targetCol = targetEntity.getCol();

        // Check all possible wrapped-around distances and get the minimum
        int dRow = Math.min(Math.abs(sourceRow - targetRow), maxRows - Math.abs(sourceRow - targetRow));
        int dCol = Math.min(Math.abs(sourceCol - targetCol), maxCols - Math.abs(sourceCol - targetCol));

        int actualProximity = Math.max(dRow, dCol);

        return actualProximity <= proximity;

    }

    private void performActions(List<Action> actions, EntityInstance entityInstance, ActionContext actionContext) {
        for (Action action : actions)
            action.invoke(entityInstance, actionContext);
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
