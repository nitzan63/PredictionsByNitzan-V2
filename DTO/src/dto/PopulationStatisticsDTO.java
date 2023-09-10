package dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationStatisticsDTO {
    private Map<Integer, Integer> ticksToPopulationMap;
    private String EntityName;

    public PopulationStatisticsDTO(String entityName , Map<Integer, Integer> ticksToPopulationMap) {
        this.EntityName = entityName;
        this.ticksToPopulationMap = ticksToPopulationMap;
    }

    public String getEntityName() {
        return EntityName;
    }

    public Map<Integer, Integer> getTicksToPopulationMap (){
        return ticksToPopulationMap;
    }

    @Override
    public String toString() {
        return "PopulationStatisticsDTO{" +
                "ticksToPopulationMap=" + ticksToPopulationMap +
                ", EntityName='" + EntityName + '\'' +
                '}';
    }
}
