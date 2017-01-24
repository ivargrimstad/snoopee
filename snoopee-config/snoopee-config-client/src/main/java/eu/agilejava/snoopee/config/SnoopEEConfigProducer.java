/*
 * The MIT License
 *
 * Copyright 2017 Ivar Grimstad (ivar.grimstad@cybercom.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.agilejava.snoopee.config;

import eu.agilejava.snoopee.SnoopEEConfigurationException;
import eu.agilejava.snoopee.annotation.SnoopEE;
import eu.agilejava.snoopee.client.SnoopEEServiceClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@cybercom.com)
 */
@ApplicationScoped
public class SnoopEEConfigProducer {

    @Inject
    @SnoopEE(serviceName = "snoopee-config")
    private SnoopEEServiceClient configService;

    private Map<String, String> configurations = new HashMap<>();

    @Produces
    @SnoopEEConfig
    public String getStringConfigValue(final InjectionPoint ip) {
        return getValue(ip.getAnnotated().getAnnotation(SnoopEEConfig.class).key());
    }

    @Produces
    @SnoopEEConfig
    public int getIntConfigValue(InjectionPoint ip) {
        return Integer.parseInt(getStringConfigValue(ip));
    }

    private String getValue(final String key) {

        if (!configurations.containsKey(key)) {
            throw new SnoopEEConfigurationException("No value for key=" + key);
        }

        return configurations.get(key);
    }

    @PostConstruct
    private void init() {
//        configurations.put("jalla", configService.getServiceRoot().toString());

        configurations.putAll(configService.simpleGet("services/helloworld/configurations")
                .filter(r -> r.getStatus() == 200)
                .map(r -> r.readEntity(new GenericType<List<Configuration>>() {}))
                .orElseThrow(SnoopEEConfigurationException::new)
                .stream()
                .collect(toMap(Configuration::getKey, Configuration::getValue)));
    }
}
