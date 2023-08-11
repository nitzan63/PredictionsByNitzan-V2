package world.utils.context;

import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.property.api.EnvProperty;

public interface Context {
    EntityInstance getPrimaryEntityInstance();
    void removeEntity(EntityInstance entityInstance);
    EnvProperty getEnvProperty(String name);
}
