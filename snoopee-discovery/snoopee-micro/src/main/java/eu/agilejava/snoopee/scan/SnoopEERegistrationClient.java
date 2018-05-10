/*
 * The MIT License
 *
 * Copyright 2015 Ivar Grimstad (ivar.grimstad@gmail.com).
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
package eu.agilejava.snoopee.scan;

import eu.agilejava.snoopee.SnoopEEConfigurationException;
import eu.agilejava.snoopee.annotation.SnoopEEClient;
import eu.agilejava.snoopee.client.SnoopEEConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Registers with SnoopEE and gives heartbeats every 10 second.
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
@SnoopEEClient
public class SnoopEERegistrationClient {

    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");

    @Inject
    @ConfigProperty(name = "snoopeeService", defaultValue = "http://localhost:8081/snoopee-service/jalla")
    private String serviceUrl;

    @Inject
    @ConfigProperty(name="port")
    private int port;

    @Inject
    @ConfigProperty(name="host")
    private String host;

    @Inject
    @ConfigProperty(name = "serviceRoot", defaultValue = "/")
    private String serviceRoot;

    private final SnoopEEConfig applicationConfig = new SnoopEEConfig();

    private Timer timer;

    @Inject
    private Event<SnoopEEConfig> configuredEvent;

    private void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOGGER.config("Checking if SnoopEE is enabled");

        if (SnoopEEExtensionHelper.isSnoopEnabled()) {

            Client client = ClientBuilder.newClient();

            try {
                readConfiguration();
                LOGGER.config(() -> "Registering " + applicationConfig.getServiceName());

                Response response = client
                        .target(serviceUrl)
                        .path("api")
                        .path("services")
                        .request()
                        .post(Entity.entity(applicationConfig, APPLICATION_JSON));

                LOGGER.config(() -> "Fire health event");
                configuredEvent.fire(applicationConfig);

            } catch (SnoopEEConfigurationException e) {
                LOGGER.severe(() -> "SnoopEE is enabled but not configured properly: " + e.getMessage());
            } finally {
                client.close();
            }

        } else {
            LOGGER.config("SnoopEE is not enabled. Use @EnableSnoopEEClient!");
        }
    }

    private void health() {

        LOGGER.config(() -> "health update: " + Calendar.getInstance().getTime());
        Client client = ClientBuilder.newClient();
        try {
            Response response = client
                    .target(serviceUrl)
                    .path("api")
                    .path("services")
                    .path(applicationConfig.getServiceName())
                    .request()
                    .put(Entity.entity(applicationConfig, APPLICATION_JSON));
        } finally {
            client.close();
        }
    }

    @PreDestroy
    private void deRegister() {

        LOGGER.config(() -> "deRegistering " + applicationConfig.getServiceName());

        Client client = ClientBuilder.newClient();
        try {
            Response response = ClientBuilder.newClient()
                    .target(serviceUrl)
                    .path("api")
                    .path("services")
                    .path(applicationConfig.getServiceName())
                    .request()
                    .delete();
        } finally {
            client.close();
        }
    }

    private void readConfiguration() throws SnoopEEConfigurationException {

        applicationConfig.setServiceName(SnoopEEExtensionHelper.getServiceName());
        applicationConfig.setServiceHome(host + ":" + port);
        applicationConfig.setServiceRoot(serviceRoot);

        LOGGER.config(() -> "application config: " + applicationConfig.toJSON());
    }

    public void init(@Observes SnoopEEConfig configEvent) {

        LOGGER.config("EVENT");
        TimerTask health = new HealthPing();
        timer = new Timer();
        timer.scheduleAtFixedRate(health, 0, 10000);
    }

    private final class HealthPing extends TimerTask {

        @Override
        public void run() {
            LOGGER.config(() -> "I am healthy!");
            health();
        }
    }

}
