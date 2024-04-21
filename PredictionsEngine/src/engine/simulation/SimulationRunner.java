package engine.simulation;

import dto.*;
import world.World;
import world.termination.api.Termination;
import java.time.LocalDateTime;
import java.util.*;

public class SimulationRunner implements Runnable {
    private World world;
    private final Termination termination;
    private int tickNumber;
    private long startTime;
    private final List<ErrorDTO> sharedErrorList;
    private volatile boolean shouldPause = false;
    private volatile boolean shouldStop = false;
    private final Object lock = new Object();
    private long pauseTime;



    public SimulationRunner(World world, List<ErrorDTO> sharedErrorList) {
        this.world = world;
        this.termination = world.getTermination();
        this.sharedErrorList = sharedErrorList;
    }

    @Override
    public void run() {
        prepareRun();
        executeRun();
    }

    private void prepareRun() {
        tickNumber = 0;
        startTime = System.currentTimeMillis();
    }

    private void executeRun() {
        while (shouldContinue()) {
            tickNumber++;
            int elapsedSeconds = getElapsedSeconds();
            if (!termination.isNotTerminated(tickNumber, elapsedSeconds)) {
                break;
            }
            try {
                world.simulateThisTick(tickNumber);
                world.setSecondsElapsed(elapsedSeconds);
            } catch (Exception e) {
                logError(e);
            }
        }
    }

    private void logError(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now(), world.getRunID());
        synchronized (sharedErrorList) {
            sharedErrorList.add(errorDTO);
        }
    }

    private boolean shouldContinue() {
        synchronized (lock) {
            if (shouldStop) {
                return false;
            }
            while (shouldPause) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return true;
    }

    private int getElapsedSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return (int) (elapsedTime / 1000);
    }

    public void pause() {
        synchronized (lock) {
            shouldPause = true;
            pauseTime = System.currentTimeMillis();
        }
    }

    public void resume() {
        synchronized (lock) {
            shouldPause = false;
            startTime += (System.currentTimeMillis() - pauseTime);
            lock.notifyAll();
        }
    }

    public void stop() {
        synchronized (lock) {
            shouldStop = true;
            lock.notifyAll();
        }
    }

    public void progressOneTick() {
        synchronized (lock) {
            if (shouldPause) {
                tickNumber++;
                int elapsedSeconds = getElapsedSeconds();
                try {
                    world.simulateThisTick(tickNumber);
                    world.setSecondsElapsed(elapsedSeconds);
                } catch (Exception e) {
                    logError(e);
                }
            }
        }
    }


}
