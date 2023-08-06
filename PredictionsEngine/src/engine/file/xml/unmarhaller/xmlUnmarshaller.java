package engine.file.xml.unmarhaller;

import scheme.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class xmlUnmarshaller {
    private final static String JAXB_XML_PACKAGE_NAME = "src.scheme.generated";
    public static void unmarshallToJava(String filePath) {
        try {
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            PRDWorld prdWorld = (PRDWorld) u.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
            //TODO: Handle Exception
        }
    }
}
