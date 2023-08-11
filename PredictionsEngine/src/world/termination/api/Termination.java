package world.termination.api;

public interface Termination {
    public boolean isNotTerminated(int tickNumber, int elapsedSeconds);
}
