package dto;

public class TerminationDTO {
    private Integer maxTicks;
    private Integer maxTime;

    public TerminationDTO(Integer maxTicks, Integer maxTime) {
        this.maxTicks = maxTicks;
        this.maxTime = maxTime;
    }

    public Integer getMaxTicks() {
        return maxTicks;
    }

    public Integer getMaxTime() {
        return maxTime;
    }
}
