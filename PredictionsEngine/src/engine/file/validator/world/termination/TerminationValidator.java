package engine.file.validator.world.termination;

import scheme.generated.PRDBySecond;
import scheme.generated.PRDByTicks;
import scheme.generated.PRDTermination;

import javax.xml.bind.ValidationException;

public class TerminationValidator {
    public static void validateTermination(PRDTermination termination) throws ValidationException {
        if (termination.getPRDBySecondOrPRDByTicks().isEmpty()) {
            if (termination.getPRDByUser() == null)
                throw new ValidationException("Termination should have at least one child: PRD-by-ticks or PRD-by-second.");
        }

        if (termination.getPRDBySecondOrPRDByTicks().size() > 2) {
            throw new ValidationException("Termination can have at most 2 children: PRD-by-ticks or PRD-by-second.");
        }

        for (Object termType : termination.getPRDBySecondOrPRDByTicks()) {
            if (termType instanceof PRDByTicks) {
                validateByTicks((PRDByTicks) termType);
            } else if (termType instanceof PRDBySecond) {
                validateBySecond((PRDBySecond) termType);
            } else {
                throw new ValidationException("Invalid termination type found.");
            }
        }
    }

    private static void validateByTicks(PRDByTicks byTicks) throws ValidationException {
        if (byTicks.getCount() <= 0) {
            throw new ValidationException("PRD-by-ticks count should be greater than 0.");
        }
    }

    private static void validateBySecond(PRDBySecond bySecond) throws ValidationException {
        if (bySecond.getCount() <= 0) {
            throw new ValidationException("PRD-by-second count should be greater than 0.");
        }
    }
}
