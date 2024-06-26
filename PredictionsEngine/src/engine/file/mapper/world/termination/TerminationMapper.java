package engine.file.mapper.world.termination;

import scheme.generated.PRDBySecond;
import scheme.generated.PRDByTicks;
import scheme.generated.PRDTermination;
import world.termination.api.Termination;
import world.termination.impl.TerminationByTicks;
import world.termination.impl.TerminationByTime;
import world.termination.impl.TerminationByUser;
import world.termination.impl.TerminationCombined;

import java.util.List;

public class TerminationMapper {
    public static Termination mapTermination(PRDTermination prdTermination){
        if (prdTermination.getPRDByUser() != null){
            return new TerminationByUser();
        }
        List<Object> terminationConditions = prdTermination.getPRDBySecondOrPRDByTicks();
        int maxTick = 0;
        int maxTime = 0;
        for (Object termination : terminationConditions){
            if (termination instanceof PRDByTicks){
                maxTick = ((PRDByTicks) termination).getCount();
            } else if (termination instanceof PRDBySecond){
                maxTime = ((PRDBySecond)termination).getCount();
            }
        }
        if (maxTime != 0 && maxTick != 0){
            return new TerminationCombined(maxTick, maxTime);
        } else if (maxTime != 0){
            return new TerminationByTime(maxTime);
        } else if (maxTick != 0){
            return new TerminationByTicks(maxTick);
        }

        return null;
    }
}
