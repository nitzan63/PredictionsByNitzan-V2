package dto;

public class EntityPropertyDTO {
    private String name;
    private String type;
    private double rangeFrom;
    private double rangeTo;
    private boolean isRandomlyInitialized;

    public EntityPropertyDTO(String name, String type, double rangeFrom, double rangeTo, boolean isRandomlyInitialized) {
        this.name = name;
        this.type = type;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.isRandomlyInitialized = isRandomlyInitialized;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(double rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public double getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(double rangeTo) {
        this.rangeTo = rangeTo;
    }

    public boolean isRandomlyInitialized() {
        return isRandomlyInitialized;
    }

    public void setRandomlyInitialized(boolean randomlyInitialized) {
        isRandomlyInitialized = randomlyInitialized;
    }

    @Override
    public String toString() {
        return "PropertyDTO{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rangeFrom=" + rangeFrom +
                ", rangeTo=" + rangeTo +
                ", isRandomlyInitialized=" + isRandomlyInitialized +
                '}';
    }
}
