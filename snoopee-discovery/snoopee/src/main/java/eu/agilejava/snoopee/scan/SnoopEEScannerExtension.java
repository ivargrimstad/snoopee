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

import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import eu.agilejava.snoopee.annotation.EnableSnoopEEClient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;

/**
 * CDI Extension that scans for @EnableSnoopEEClient annotations.
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
public class SnoopEEScannerExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");

    private String serviceName;
    private boolean snoopEnabled;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        LOGGER.config("Scanning for SnoopEE clients");
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {

        LOGGER.config("Discovering SnoopEE clients");
        SnoopEEExtensionHelper.setServiceName(serviceName);
        SnoopEEExtensionHelper.setSnoopEnabled(snoopEnabled);

        AnnotatedType<SnoopEERegistrationClient> at = bm.createAnnotatedType(SnoopEERegistrationClient.class);
        final InjectionTarget<SnoopEERegistrationClient> it = bm.createInjectionTarget(at);
       
        abd.addBean(new Bean<SnoopEERegistrationClient>() {
            @Override
            public Class<?> getBeanClass() {
                return SnoopEERegistrationClient.class;
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            @Override
            public boolean isNullable() {
                return false;
            }

            @Override
            public SnoopEERegistrationClient create(CreationalContext<SnoopEERegistrationClient> creationalContext) {
                SnoopEERegistrationClient instance = it.produce(creationalContext);
                it.inject(instance, creationalContext);
                it.postConstruct(instance);
                
                return instance;
            }

            @Override
            public void destroy(SnoopEERegistrationClient instance, CreationalContext<SnoopEERegistrationClient> creationalContext) {
                it.preDestroy(instance);
                it.dispose(instance);
                creationalContext.release();
            }

            @Override
            public Set<Type> getTypes() {
                Set<Type> types = new HashSet<Type>();
                types.add(SnoopEERegistrationClient.class);
                types.add(Object.class);
                return types;
            }

            @Override
            public Set<Annotation> getQualifiers() {
                Set<Annotation> qualifiers = new HashSet<Annotation>();
                qualifiers.add(new AnnotationLiteral<Default>() {
                });
                qualifiers.add(new AnnotationLiteral<Any>() {
                });
                return qualifiers;
            }

            @Override
            public Class<? extends Annotation> getScope() {
                return ApplicationScoped.class;
            }

            @Override
            public String getName() {
                return "snoopEERegistrationClient";
            }

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            @Override
            public boolean isAlternative() {
                return false;
            }

        });

        LOGGER.config("Finished scanning for SnoopEE clients");
    }

    <T> void processAnnotatedType(@Observes @WithAnnotations(EnableSnoopEEClient.class) ProcessAnnotatedType<T> pat) {

        // workaround for WELD bug revealed by JDK8u60
        final ProcessAnnotatedType<T> snoopAnnotated = pat;

        LOGGER.config(() -> "Found @EnableSnoopEEClient annotated class: " + snoopAnnotated.getAnnotatedType().getJavaClass().getName());
        snoopEnabled = true;
        serviceName = snoopAnnotated.getAnnotatedType().getAnnotation(EnableSnoopEEClient.class).serviceName();
        LOGGER.config(() -> "SnoopEE Service name is: " + serviceName);
    }
}
