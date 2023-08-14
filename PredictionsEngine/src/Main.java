import engine.file.xml.unmarshaller.xmlUnmarshaller;
import engine.mapper.world.WorldMapper;
import engine.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;
import world.utils.time.TimeUtils;

public class Main {
    public static void main(String[] args) {

        String xmlFilePath = "/Users/nitzanainemer/IdeaProjects/PredictionsByNitzan/PredictionsEngine/src/scheme/xml/ex1-error-6.xml";
        PRDWorld prdWorld = xmlUnmarshaller.unmarshallToJava(xmlFilePath);
        World world;

        if (XMLValidator.validateXML(prdWorld)) {
            System.out.println("XML validation Passed!");
            world = WorldMapper.mapWorld(prdWorld);
            System.out.println(world);
        } else {
            System.out.println("XML validation Failed.");
        }



    }
}