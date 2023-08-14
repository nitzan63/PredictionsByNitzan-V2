import engine.file.xml.unmarshaller.xmlUnmarshaller;
import engine.mapper.world.WorldMapper;
import engine.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;
import world.utils.expression.ExpressionEvaluator;
import world.utils.time.TimeUtils;

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