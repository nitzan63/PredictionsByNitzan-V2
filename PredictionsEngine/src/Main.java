import engine.file.xml.unmarshaller.xmlUnmarshaller;
import engine.mapper.world.WorldMapper;
import engine.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;
import world.utils.time.TimeUtils;

public class Main {
    public static void main(String[] args) {

        String xmlFilePath = "/Users/nitzanainemer/IdeaProjects/PredictionsByNitzan/PredictionsEngine/src/scheme/xml/ex1-cigarets.xml";
        PRDWorld prdWorld = xmlUnmarshaller.unmarshallToJava(xmlFilePath);
        World world;

        if (XMLValidator.validateXML(prdWorld)){
            System.out.println("XML validation Passed!");
        } else {
            System.out.println("XML validation Failed.");
        }

        world = WorldMapper.mapWorld(prdWorld);
        System.out.println(world);

//        int tickNumber = 0;
//        long startTimeMillis = System.currentTimeMillis();
//        while (world.getTermination().isNotTerminated(tickNumber, TimeUtils.getElapsedSeconds(startTimeMillis))){
//            world.simulateThisTick(tickNumber);
//            tickNumber++;
//        }
    }
}