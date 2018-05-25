package eu.agilejava.snoopee.config;

import eu.agilejava.snoopee.SnoopEEConfigurationException;
import eu.agilejava.snoopee.SnoopEEExtensionHelper;
import eu.agilejava.snoopee.annotation.SnoopEE;
import eu.agilejava.snoopee.client.SnoopEEServiceClient;
import org.eclipse.microprofile.config.spi.ConfigSource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class SnoopEEConfigSource implements ConfigSource {

    private static final Logger LOGGER = Logger.getLogger(SnoopEEConfigSource.class.getName());

    @Inject
    @SnoopEE(serviceName = "snoopee-config")
    private SnoopEEServiceClient configService;

    private final Map<String, String> configurations = new HashMap<>();

    @Override
    public Map<String, String> getProperties() {
        return configurations;
    }

    @Override
    public String getValue(String key) {
        if (configurations.containsKey(key)) {
            return configurations.get(key);
        } else {
            LOGGER.severe(() -> "No value for key: " + key);
            throw new SnoopEEConfigurationException("No value for key:" + key);
        }
    }

    @Override
    public String getName() {
        return "SnoopEE Config Source";
    }

    @Override
    public int getOrdinal() {
        return 350;
    }

    @PostConstruct
    private void init() {

        try {

            configurations.putAll(configService.simpleGet("services/" + SnoopEEExtensionHelper.getServiceName() + "/configurations")
                    .filter(r -> r.getStatus() == 200)
                    .map(r -> r.readEntity(new GenericType<List<Configuration>>() {
                    }))
                    .get()
                    .stream()
                    .collect(toMap(Configuration::getKey, Configuration::getValue)));

        } catch (NoSuchElementException e) {
            LOGGER.warning(() -> "No configurations found for service: " + SnoopEEExtensionHelper.getServiceName());
        }
    }
}