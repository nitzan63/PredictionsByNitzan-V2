package world.termination.impl;

import world.termination.api.Termination;

public class TerminationByUser implements Termination {
    @Override
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds) {
        return true;
    }

    @Override
    public String getTerminationMessage() {
        return "Terminated by user!";
    }
}
