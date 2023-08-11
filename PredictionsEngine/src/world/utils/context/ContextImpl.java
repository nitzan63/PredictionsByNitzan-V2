package world.utils.context;

import world.entities.entity.EntityInstance;
import world.environment.properties.property.api.EnvProperty;

public class ContextImpl implements Context{
    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return null;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {

    }

    @Override
    public EnvProperty getEnvProperty(String name) {
        return null;
    }
}
