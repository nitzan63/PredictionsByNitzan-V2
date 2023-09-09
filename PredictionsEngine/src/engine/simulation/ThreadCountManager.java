package engine.simulation;

import dto.ThreadInfoDTO;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadCountManager {
    private final ThreadPoolExecutor executorService;
    private final ThreadInfoDTO threadInfoDTO;

    public ThreadCountManager(ThreadPoolExecutor executorService, SimulationManager simulationManager) {

        this.executorService = executorService;
        this.threadInfoDTO = new ThreadInfoDTO();
        threadInfoDTO.setTotalThreads(simulationManager.getNumberOfThreads());
    }

    public void incrementQueuedSimulations() {
        threadInfoDTO.setSimulationsInQueue(threadInfoDTO.getSimulationsInQueue() + 1);
        updateThreadInfoDTO();
    }

    public void decrementQueuedSimulations() {
        threadInfoDTO.setSimulationsInQueue(threadInfoDTO.getSimulationsInQueue() - 1);
        updateThreadInfoDTO();
    }

    public void incrementTotalSimulations(){
        threadInfoDTO.incrementTotalSimulation();
    }

    private void updateThreadInfoDTO() {
        threadInfoDTO.setActiveThreads(executorService.getActiveCount());
    }

    public ThreadInfoDTO getThreadInfoDTO() {
        updateThreadInfoDTO();
        return threadInfoDTO;
    }
}
