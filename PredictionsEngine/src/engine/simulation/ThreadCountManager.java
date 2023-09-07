package engine.simulation;

import dto.ThreadInfoDTO;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadCountManager {
    private final ThreadPoolExecutor executorService;
    private final ThreadInfoDTO threadInfoDTO = new ThreadInfoDTO();

    public ThreadCountManager(ThreadPoolExecutor executorService) {
        this.executorService = executorService;
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
