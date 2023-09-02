package engine.file.mapper.world.rules.rule.activation;

import scheme.generated.PRDActivation;
import world.rules.rule.activation.Activation;

public class ActivationMapper {
    public static Activation mapActivation(PRDActivation jaxbActivation){
        double probability = (jaxbActivation.getProbability() != null) ? jaxbActivation.getProbability() : 1;
        int ticksToActivate = (jaxbActivation.getTicks() != null ) ? jaxbActivation.getTicks() :  1;
        return new Activation(probability, ticksToActivate);
    }
}
