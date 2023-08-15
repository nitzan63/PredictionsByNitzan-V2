package dto;

public class PopulationStatisticsDTO {
    private int initialPopulation;
    private int finalPopulation;

    public PopulationStatisticsDTO(int initialPopulation, int finalPopulation) {
        this.initialPopulation = initialPopulation;
        this.finalPopulation = finalPopulation;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(int initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public int getFinalPopulation() {
        return finalPopulation;
    }

    public void setFinalPopulation(int finalPopulation) {
        this.finalPopulation = finalPopulation;
    }

    @Override
    public String toString() {
        return "PopulationStatisticsDTO{" +
                "initialPopulation=" + initialPopulation +
                ", finalPopulation=" + finalPopulation +
                '}';
    }
}
