package dto;

public class ThreadInfoDTO {
    private int totalThreads;
    private int activeThreads;
    private int simulationsInQueue;
    private int totalSimulations;

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

    public int getTotalSimulations() {
        return totalSimulations;
    }

    public void setTotalSimulations(int totalSimulations) {
        this.totalSimulations = totalSimulations;
    }

    public void incrementTotalSimulation(){
        this.totalSimulations ++;
    }
}
