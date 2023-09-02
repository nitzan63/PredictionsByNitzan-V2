package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.generator.EntityGenerator;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class ReplaceAction extends AbstractAction{
    private final String killEntityName;
    private final String createEntityName;
    private final String mode;

    public ReplaceAction(EntitiesDefinition entitiesDefinition, String killEntityName, String createEntityName, String mode) {
        super(ActionType.REPLACE, entitiesDefinition, killEntityName);
        this.killEntityName = killEntityName;
        this.createEntityName = createEntityName;
        this.mode = mode;
    }

    @Override
    public void invoke(EntityInstance entityInstance, Environment environment) {
        // Step 1: Kill the target entity
        entitiesDefinition.removeEntityInstance(entityInstance.getSerialNumber());
        entitiesDefinition.setPopulation(entitiesDefinition.getPopulation() - 1);

        EntityGenerator entityGenerator = new EntityGenerator();

        // Step 2: Create a new entity:
        EntityInstance newInstance;
        if ("scratch".equals(mode)){
            newInstance = entityGenerator.replaceFromScratch(createEntityName);
        } else if ("derived".equals(mode)){
            newInstance = entityGenerator.replaceDerived(createEntityName, entityInstance);
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        // Step 3: Add the new entity instance to the entities:
        entitiesDefinition.addEntity(newInstance, newInstance.getSerialNumber());
        entitiesDefinition.setPopulation(entitiesDefinition.getPopulation() + 1);

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