import engine.file.unmarshaller.xmlUnmarshaller;
import engine.file.mapper.world.WorldMapper;
import engine.file.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;

public class Main {
    public static void main(String[] args) {

        String xmlFilePath = "/Users/nitzanainemer/IdeaProjects/PredictionsByNitzan/PredictionsEngine/src/scheme/xml/ex1-cigarets.xml";
        PRDWorld prdWorld = xmlUnmarshaller.unmarshallToJava(xmlFilePath);
        World world;

        if (XMLValidator.validateXML(prdWorld)) {
            System.out.println("XML validation Passed!");
            world = WorldMapper.mapWorld(prdWorld);
            System.out.println(world);
            System.out.println("\n\n----evaluation test-----\n\n");
            // System.out.println(ExpressionEvaluator.evaluateExpression("environment(cigarets-increase-already-smoker)", world.getEntities().getEntity(1)));
            System.out.println("\n\n\n---------------Starting Simulation---------------\n\n\n");
            int tick = 1;
            while (world.getTermination().isNotTerminated(tick, 1)){
                System.out.println("Tick number: " + tick + " Population left: " + world.getEntities().getPopulation());
                world.simulateThisTick(1);
                tick ++;
            }
            System.out.println("\n\n\n---------------FINISHED---------------\n\n\n");
            System.out.println("Population left: " + world.getEntities().getPopulation());
            System.out.println(world);
        } else {
            System.out.println("XML validation Failed.");
        }



    }
}