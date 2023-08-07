package engine.file.xml.unmarshaller;

import scheme.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class xmlUnmarshaller {
    private final static String JAXB_XML_PACKAGE_NAME = "scheme.generated";
    public static PRDWorld unmarshallToJava(String filePath) {
        try {
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            return (PRDWorld) u.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
            //TODO: Handle Exception
        }
    }
}
