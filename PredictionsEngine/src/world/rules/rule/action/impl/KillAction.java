package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class KillAction extends AbstractAction {
    public KillAction(EntitiesDefinition entitiesDefinition) {super(ActionType.KILL, entitiesDefinition);}

    @Override
    public void invoke(EntityInstance entityInstance) {

    }
}
