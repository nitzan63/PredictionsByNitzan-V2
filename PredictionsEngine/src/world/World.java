package world;

import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.rules.Rules;
import world.termination.api.Termination;

public class World {
    private Environment environment;
    private EntitiesDefinition entities;
    private Rules rules;
    private Termination termination;

}
