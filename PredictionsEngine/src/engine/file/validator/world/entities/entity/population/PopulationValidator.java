package engine.file.validator.world.entities.entity.population;

import javax.xml.bind.ValidationException;

public class PopulationValidator {
    public static void validatePopulation (int population) throws ValidationException {
        if (population < 0)
            throw new ValidationException("Population must be non-negative.");
    }
}
