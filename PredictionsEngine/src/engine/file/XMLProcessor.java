package engine.file;

import engine.file.exceptions.FileValidationException;
import engine.file.exceptions.XMLProcessingException;
import engine.file.mapper.world.WorldMapper;
import engine.file.unmarshaller.xmlUnmarshaller;
import engine.file.validator.PostXMLMappingValidatorV2;
import engine.file.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;

import javax.xml.bind.ValidationException;

public class XMLProcessor {
    private int numberOfThreads = 0;
    public World processXML(String filePath) throws XMLProcessingException {
        PRDWorld jaxbWorld;
        World world;
        try {
            // Step 1: Check file exists and can be loaded
            FileChecker.validateFile(filePath);
            // Step 2: Unmarshall and get number of threads
            jaxbWorld = xmlUnmarshaller.unmarshallToJava(filePath);
            numberOfThreads = jaxbWorld.getPRDThreadCount();
            // Step 3: Run validateXML
            XMLValidator.validateXML(jaxbWorld);
            // Step 4: Map
            world = WorldMapper.mapWorld(jaxbWorld);
            // Step 5: Run validateXMLPostMapping
            PostXMLMappingValidatorV2.validateXMLPostMapping(world);
        } catch (FileValidationException e) {
            throw new XMLProcessingException("File validation error: " + e.getMessage(), e);
        } catch (ValidationException e) {
            throw new XMLProcessingException("Validation error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new XMLProcessingException("Error processing XML: " + e.getMessage(), e);
        }
        return world;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }
}

