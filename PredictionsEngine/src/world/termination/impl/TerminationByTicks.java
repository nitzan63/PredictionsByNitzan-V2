package world.termination.impl;

import world.termination.api.Termination;

public class TerminationByTicks implements Termination {
private final int maxTicks;
private String terminationMessage = null;

    public TerminationByTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds)
    {
        if (tickNumber <= maxTicks){
            return true;
        } else {
            setTerminationMessage("Terminated by Ticks");
            return false;
        }
    }

    @Override
    public String getTerminationMessage() {
        return terminationMessage;
    }

    public void setTerminationMessage(String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    @Override
    public String toString() {
        return "TerminationByTicks{" +
                "maxTicks=" + maxTicks +
                '}';
    }
}
