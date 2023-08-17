package world.termination.impl;

import world.termination.api.Termination;

public class TerminationByTime implements Termination {
    private final int maxTime;

    public TerminationByTime(int maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds) {
        return elapsedSeconds <= maxTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    @Override
    public String toString() {
        return "TerminationByTime{" +
                "maxTime=" + maxTime +
                '}';
    }
}
