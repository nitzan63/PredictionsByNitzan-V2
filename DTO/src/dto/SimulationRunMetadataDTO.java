package dto;

public class SimulationRunMetadataDTO {
    private final String runIdentifier;
    private final String dateTime;
    private final String terminationReason;

    public SimulationRunMetadataDTO(String runIdentifier, String dateTime, String terminationReason) {
        this.runIdentifier = runIdentifier;
        this.dateTime = dateTime;
        this.terminationReason = terminationReason;
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

    @Override
    public String toString() {
        return "SimulationRunMetadataDTO{" +
                "runIdentifier='" + runIdentifier + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", terminationReason='" + terminationReason + '\'' +
                '}';
    }
}
