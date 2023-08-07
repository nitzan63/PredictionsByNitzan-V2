import engine.file.xml.unmarshaller.xmlUnmarshaller;
import engine.mapper.world.WorldMapper;
import engine.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;

public class Main {
    public static void main(String[] args) {

        String xmlFilePath = "/Users/nitzanainemer/IdeaProjects/PredictionsByNitzan/PredictionsEngine/src/scheme/xml/ex1-error-2.xml";
        PRDWorld prdWorld = xmlUnmarshaller.unmarshallToJava(xmlFilePath);

        if (XMLValidator.validateXML(prdWorld)){
            System.out.println("XML validation Passed!");
            World world = WorldMapper.mapWorld(prdWorld);
            System.out.println(world);
        } else {
            System.out.println("XML validation Failed.");
        }
    }
}