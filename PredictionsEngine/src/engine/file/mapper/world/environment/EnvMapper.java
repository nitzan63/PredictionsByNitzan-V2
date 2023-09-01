package engine.file.mapper.world.environment;

import engine.file.mapper.world.environment.properties.property.EnvPropertyMapper;
import scheme.v1.generated.PRDEnvProperty;
import scheme.v1.generated.PRDEvironment;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;

public class EnvMapper {
    public static Environment mapEnvironment(PRDEvironment jaxbEnv) {
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
