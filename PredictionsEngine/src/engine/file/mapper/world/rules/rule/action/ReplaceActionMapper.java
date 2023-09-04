package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.rules.rule.action.impl.ReplaceAction;
import world.rules.rule.action.secondary.SecondaryEntity;

public class ReplaceActionMapper {
    public static ReplaceAction mapReplaceAction(PRDAction jaxbAction, SecondaryEntity secondaryEntity){

        String killEntity = jaxbAction.getKill();
        String createEntity = jaxbAction.getCreate();
        String mode = jaxbAction.getMode();

        return new ReplaceAction(killEntity, createEntity, mode, secondaryEntity);
    }
}
