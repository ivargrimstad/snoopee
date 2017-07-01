/*
 * The MIT License
 *
 * Copyright 2017 Ivar Grimstad (ivar.grimstad@gmail.com).
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
package eu.agilejava.snoopee.config.cdi;

import eu.agilejava.snoopee.config.convert.StringConverter;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
public class SnoopEEConfigExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");

     public void registerConfigProducer(@Observes AfterBeanDiscovery abd, BeanManager bm) {
         
//         abd.addBean(new StringConverter());
//        Set<Class> types = injectionPoints.stream()
//                .filter(ip -> ip.getType() instanceof Class)
//                .map(ip -> (Class) ip.getType())
//                .collect(Collectors.toSet());
//
//        // Provider and Optional are ParameterizedTypes and not a Class, so we need to add them manually
//        types.add(Provider.class);
//        types.add(Optional.class);

//        types.forEach(type -> abd.addBean(new ConfigInjectionBean(bm, type)));
    }
}
