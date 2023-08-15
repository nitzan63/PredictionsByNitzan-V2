import engine.file.XMLProcessor;
import engine.file.exceptions.XMLProcessingException;
import engine.file.unmarshaller.xmlUnmarshaller;
import engine.file.mapper.world.WorldMapper;
import engine.file.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;

import javax.xml.bind.ValidationException;

public class Main {
    public static void main(String[] args) {

        String filePath = "/Users/nitzanainemer/IdeaProjects/PredictionsByNitzan/PredictionsEngine/src/scheme/xml/ex1-cigarets.xml";

        XMLProcessor processor = new XMLProcessor();

        try {
            World world = processor.processXML(filePath);
            System.out.println("XML processed successfully and returned a World object: " + world);
        } catch (XMLProcessingException e) {
            System.err.println("Error processing XML: " + e.getMessage());
            Throwable cause = e.getCause();
            if (cause != null) {
                System.err.println("Cause: " + cause.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

            // System.out.println(ExpressionEvaluator.evaluateExpression("environment(cigarets-increase-already-smoker)", world.getEntities().getEntity(1)));
//            System.out.println("\n\n\n---------------Starting Simulation---------------\n\n\n");
//            int tick = 1;
//            while (world.getTermination().isNotTerminated(tick, 1)){
//                System.out.println("Tick number: " + tick + " Population left: " + world.getEntities().getPopulation());
//                world.simulateThisTick(1);
//                tick ++;
//            }
//            System.out.println("\n\n\n---------------FINISHED---------------\n\n\n");
//            System.out.println("Population left: " + world.getEntities().getPopulation());
//            System.out.println(world);
//        } else {
//            System.out.println("XML validation Failed.");
//        }



    }
}