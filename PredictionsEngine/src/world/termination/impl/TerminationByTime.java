package world.termination.impl;

import world.termination.api.Termination;

public class TerminationByTime implements Termination {
    private final int maxTime;
    private String terminationMessage = null;

    @Override
    public String getTerminationMessage() {
        return terminationMessage;
    }

    public void setTerminationMessage(String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    public TerminationByTime(int maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds)
    {
        if (elapsedSeconds <= maxTime){
            return true;
        } else {
            setTerminationMessage("Terminated by Time");
            return false;
        }
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
