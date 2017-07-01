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
package eu.agilejava.snoopee.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
public class SnoopEEConfigBuilder implements ConfigBuilder {

    @Override
    public ConfigBuilder addDefaultSources() {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader loader) {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... sources) {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        return new SnoopEEConfigBuilder();
    }

    @Override
    public Config build() {
        return new SnoopEEConfig();
    }
    
    
}
