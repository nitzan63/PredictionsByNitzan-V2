package world.rules.rule.actions.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.actions.action.api.AbstractAction;
import world.rules.rule.actions.action.api.ActionType;

public class KillAction extends AbstractAction {
    public KillAction(EntitiesDefinition entitiesDefinition) {super(ActionType.KILL, entitiesDefinition);}

    @Override
    public void invoke(EntityInstance entityInstance) {

    }
}
