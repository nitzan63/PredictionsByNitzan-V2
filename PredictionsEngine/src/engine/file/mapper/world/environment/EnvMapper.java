package engine.file.mapper.world.environment;

import engine.file.mapper.world.environment.properties.property.EnvPropertyMapper;
import scheme.generated.PRDEnvProperty;
import scheme.generated.PRDEnvironment;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;

public class EnvMapper {
    public static Environment mapEnvironment(PRDEnvironment jaxbEnv) {
        Environment env = new Environment();
        EnvProperties envProp = new EnvProperties();
        for (PRDEnvProperty jaxbProp : jaxbEnv.getPRDEnvProperty()) {
            EnvProperty property = EnvPropertyMapper.mapEnvProperty(jaxbProp);
            envProp.addProperty(property);
        }
        env.setProperties(envProp);
        return env;
    }
}
