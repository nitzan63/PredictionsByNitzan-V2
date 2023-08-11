package world.termination.impl;

import world.termination.api.Termination;

public class TerminationByTicks implements Termination {
private final int maxTicks;

    public TerminationByTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds) {
        return tickNumber <= maxTicks;
    }

    @Override
    public String toString() {
        return "TerminationByTicks{" +
                "maxTicks=" + maxTicks +
                '}';
    }
}
