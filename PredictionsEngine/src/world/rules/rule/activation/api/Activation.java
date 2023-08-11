package world.rules.rule.activation.api;

public interface Activation {
    boolean isActive(int tickNumber);
    int getTicks();
    double getProbability();
}
