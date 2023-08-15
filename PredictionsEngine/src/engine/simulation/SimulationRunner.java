package engine.simulation;

import world.World;
import world.termination.api.Termination;

public class SimulationRunner {
    private final World world;
    private final Termination termination;
    private int tickNumber;
    private final long startTime;

    public SimulationRunner(World world) {
        this.world = world;
        this.termination = world.getTermination();
        this.tickNumber = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void runSimulation() {
        while (shouldContinue()) {
            tickNumber++;
            int elapsedSeconds = getElapsedSeconds();
            if (!termination.isNotTerminated(tickNumber, elapsedSeconds)) {
                break;
            }
            try {
                world.simulateThisTick(tickNumber);
            } catch (Exception e) {
                System.err.println("An error occurred during simulation at tick " + tickNumber + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean shouldContinue() {
        // Add any other conditions to check before each tick
        return true;
    }

    private int getElapsedSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return (int) (elapsedTime / 1000);
    }
}
