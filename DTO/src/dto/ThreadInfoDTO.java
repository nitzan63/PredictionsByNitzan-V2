package dto;

public class ThreadInfoDTO {
    private int totalThreads;
    private int activeThreads;
    private int simulationsInQueue;

    public int getTotalThreads() {
        return totalThreads;
    }

    public void setTotalThreads(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(int activeThreads) {
        this.activeThreads = activeThreads;
    }

    public int getSimulationsInQueue() {
        return simulationsInQueue;
    }

    public void setSimulationsInQueue(int simulationsInQueue) {
        this.simulationsInQueue = simulationsInQueue;
    }
}
