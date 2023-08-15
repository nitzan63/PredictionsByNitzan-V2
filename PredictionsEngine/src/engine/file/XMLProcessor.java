package engine.file;

import engine.file.mapper.world.WorldMapper;
import engine.file.unmarshaller.xmlUnmarshaller;
import engine.file.validator.PostXMLMappingValidator;
import engine.file.validator.XMLValidator;
import scheme.generated.PRDWorld;
import world.World;

import javax.xml.bind.ValidationException;
import java.util.List;

public class XMLProcessor {
    public World processXML(String filePath) throws XMLProcessingException {
        PRDWorld jaxbWorld;
        World world;
        try {
            // Step 1: Check file exists and can be loaded
            FileChecker.validateFile(filePath);
            // Step 2: Unmarshall
            jaxbWorld = xmlUnmarshaller.unmarshallToJava(filePath);
            // Step 3: Run validateXML
            List<ValidationException> exceptions = XMLValidator.validateXML(jaxbWorld);
            if (!exceptions.isEmpty()) {
                throw new XMLProcessingException("XML validation failed", exceptions.get(0));
            }
            // Step 4: Map
            world = WorldMapper.mapWorld(jaxbWorld);
            // Step 5: Run validateXMLPostMapping
            PostXMLMappingValidator.validateXMLPostMapping(world);
        } catch (FileValidationException e) {
            throw new XMLProcessingException("File validation error: " + e.getMessage(), e);
        } catch (ValidationException e) {
            throw new XMLProcessingException("Validation error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new XMLProcessingException("Error processing XML: " + e.getMessage(), e);
        }

        return world;
    }
}

class XMLProcessingException extends Exception {
    public XMLProcessingException(String message) {
        super(message);
    }

    public XMLProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
