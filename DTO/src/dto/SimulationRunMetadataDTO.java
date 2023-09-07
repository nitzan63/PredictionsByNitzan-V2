package dto;

public class SimulationRunMetadataDTO {
    private final String runIdentifier;
    private final String dateTime;
    private String terminationReason;

    public SimulationRunMetadataDTO(String runIdentifier, String dateTime) {
        this.runIdentifier = runIdentifier;
        this.dateTime = dateTime;
        this.terminationReason = null;
    }

    public String getRunIdentifier() {
        return runIdentifier;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    @Override
    public String toString() {
        return "SimulationRunMetadataDTO{" +
                "runIdentifier='" + runIdentifier + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", terminationReason='" + terminationReason + '\'' +
                '}';
    }
}
