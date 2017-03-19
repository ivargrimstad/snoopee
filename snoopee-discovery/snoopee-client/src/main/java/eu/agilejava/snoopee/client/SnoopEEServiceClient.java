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
package eu.agilejava.snoopee.client;

import java.util.Optional;
import java.util.logging.Logger;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 * Client API for calling services registered with SnoopEE.
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
public class SnoopEEServiceClient {

    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");
    private static final String DEFAULT_BASE_URI = "http://localhost:8080/snoopee-service/";
    private static final String DEFAULT_ENCODING = "charset=utf8";

    private final String applicationName;
    private final String serviceUrl;

    static final class Builder {

        private final String applicationName;
        private String serviceUrl = DEFAULT_BASE_URI;

        Builder(final String applicationName) {
            this.applicationName = applicationName;
        }

        Builder serviceUrl(final String serviceUrl) {
            this.serviceUrl = serviceUrl;
            return this;
        }

        SnoopEEServiceClient build() {
            return new SnoopEEServiceClient(this);
        }
    }

    private SnoopEEServiceClient(final Builder builder) {
        this.applicationName = builder.applicationName;
        this.serviceUrl = builder.serviceUrl;
        LOGGER.info(() -> "client created for " + applicationName);
    }

    /**
     * Locator to get the service root for the service registered with SnoopEE.
     *
     * Use this method if the convenience methods simpleXXX are not sufficient or to avoid the extra call to SnoopEE for
     * every request.
     *
     * @return the serviceRoot
     *
     * @throws SnoopEEServiceUnavailableException if service is not available
     */
    public WebTarget getServiceRoot() throws SnoopEEServiceUnavailableException {

        SnoopEEConfig snoopEEConfig = getConfigFromSnoopEE();
        LOGGER.fine(() -> "looking up service for " + applicationName);

        return ClientBuilder.newClient()
                .target(snoopEEConfig.getServiceHome())
                .path(snoopEEConfig.getServiceRoot());
    }

    /**
     * Convenience method for making a simple GET request on a resource.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual GET request.
     *
     * @param resourcePath The relative path to the resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> simpleGet(String resourcePath) {

        Optional<Response> returnValue = Optional.empty();

        try {

            returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .get());

        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }
    
    /**
     * Convenience method for making a simple DELETE request on a resource.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual DELETE request.
     *
     * @param resourcePath The relative path to the resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> simpleDelete(String resourcePath) {

        Optional<Response> returnValue = Optional.empty();

        try {

            returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .delete());

        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }

    /**
     * Convenience method for making a simple PUT request on a resource.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual PUT request.
     *
     * @param resourcePath The relative path to the resource
     * @param resource The changes made to this resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> simplePut(String resourcePath, Object resource) {

        Optional<Response> returnValue = Optional.empty();

        try {

            returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .put(Entity.entity(resource, APPLICATION_JSON)));

        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }

    /**
     * Convenience method for making a simple POST request on a resource.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual POST request.
     *
     * @param resourcePath The relative path to the resource
     * @param resource The new resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> simplePost(String resourcePath, Object resource) {

        Optional<Response> returnValue = Optional.empty();

        try {

            returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .post(Entity.entity(resource, APPLICATION_JSON)));

        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }
    
    private SnoopEEConfig getConfigFromSnoopEE() throws SnoopEEServiceUnavailableException {

        try {
            Response response = ClientBuilder.newClient()
                    .target(serviceUrl)
                    .path("api")
                    .path("services")
                    .path(applicationName)
                    .request(APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                return response.readEntity(SnoopEEConfig.class);
            } else {
                throw new SnoopEEServiceUnavailableException("Response from \"" + serviceUrl + "\"=" + response.getStatus());
            }

        } catch (ProcessingException e) {
            throw new SnoopEEServiceUnavailableException(e);
        }
    }
        
    /**
     * Method for making a GET request on a resource with setting explicit headers.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual GET request.
     *
     * @param headers The headers to set
     * @param resourcePath The relative path to the resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> get(MultivaluedHashMap<String, Object> headers, String resourcePath) {

    	Optional<Response> returnValue = Optional.empty();

        try {
        	returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .headers(headers)
                    .get());
        	
        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }
    
    /**
     * Method for making a DELETE request on a resource with setting explicit headers.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual DELETE request.
     *
     * @param headers The headers to set
     * @param resourcePath The relative path to the resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> delete(MultivaluedHashMap<String, Object> headers, String resourcePath) {

    	Optional<Response> returnValue = Optional.empty();

        try {
        	returnValue = Optional.of(getServiceRoot()
        			.path(resourcePath)
        			.request()
        			.headers(headers)
        			.delete());
        	
        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }

    /**
     * Method for making a simple PUT request on a resource with setting explicit headers.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual PUT request.
     *
     * @param headers The headers to set
     * @param resourcePath The relative path to the resource
     * @param resource The changes made to this resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> put(MultivaluedHashMap<String, Object> headers, String resourcePath, Object resource) {
   
        Optional<Response> returnValue = Optional.empty();

        try {
        	returnValue = Optional.of(getServiceRoot()
        			.path(resourcePath)
        			.request()
                  	.headers(headers)
                  	.put(Entity.entity(resource, APPLICATION_JSON + "; " + DEFAULT_ENCODING)));
        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }

    /**
     * Method for making a simple POST request on a resource with setting explicit headers.
     *
     * Calling this method will result in a call to SnoopEE to retrieve the current configuration for the service in
     * addition to the actual POST request.
     *
     * @param headers The headers to set
     * @param resourcePath The relative path to the resource
     * @param resource The new resource
     * @return an optional response that is empty if the service is unavailable.
     */
    public Optional<Response> post(MultivaluedHashMap<String, Object> headers, String resourcePath, Object resource) {

        Optional<Response> returnValue = Optional.empty();

        try {
        	returnValue = Optional.of(getServiceRoot()
                    .path(resourcePath)
                    .request()
                    .headers(headers)
                    .post(Entity.entity(resource, APPLICATION_JSON + "; " + DEFAULT_ENCODING)));
        } catch (SnoopEEServiceUnavailableException e) {
            LOGGER.warning(() -> "Service unavailable for " + applicationName);
        }

        return returnValue;
    }
}
