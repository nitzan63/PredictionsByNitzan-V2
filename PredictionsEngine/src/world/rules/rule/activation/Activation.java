package world.rules.rule.activation;


public class Activation {

    boolean isActive;
    private double probability;
    int ticksToActivate;

    public Activation(double probability, int ticksToActivate) {
        this.probability = probability;
        this.ticksToActivate = ticksToActivate;
    }

    public boolean isActive(int tickNumber) {
        isActive = false;
        if ((tickNumber % ticksToActivate) == 0) {
            if (probability == 1) isActive = true;
            else {
                double randomValue = Math.random();
                isActive = randomValue <= probability;
            }
        }
        return isActive;
    }

    public double getProbability() {
        return this.probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getTicksToActivate() {
        return ticksToActivate;
    }

    public void setTicksToActivate(int ticksToActivate) {
        this.ticksToActivate = ticksToActivate;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Activation{" +
                "isActive=" + isActive +
                ", probability=" + probability +
                ", ticksToActivate=" + ticksToActivate +
                '}';
    }
}
