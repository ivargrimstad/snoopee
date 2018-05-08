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
package eu.agilejava.snoopee;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
@Startup
@Singleton
public class SnoopEEService {

    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");

    @PostConstruct
    private void start() {

//        // http://www.network-science.de/ascii/ Font: big
//        LOGGER.config("  _____                         ______ ______ ");
//        LOGGER.config(" / ____|                       |  ____|  ____|");
//        LOGGER.config("| (___  _ __   ___   ___  _ __ | |__  | |__   ");
//        LOGGER.config(" \\___ \\| '_ \\ / _ \\ / _ \\| '_ \\|  __| |  __|  ");
//        LOGGER.config(" ____) | | | | (_) | (_) | |_) | |____| |____ ");
//        LOGGER.config("|_____/|_| |_|\\___/ \\___/| .__/|______|______|");
//        LOGGER.config("                         | |                  ");
//        LOGGER.config("                         |_|                  ");

        // http://www.ascii-art-generator.org/ Font: slant
        LOGGER.config(() -> "");
        LOGGER.config(() -> "   _____                         ____________   __  ____               ");
        LOGGER.config(() ->"  / ___/____  ____  ____  ____  / ____/ ____/  /  |/  (_)_____________ ");
        LOGGER.config(() ->"  \\__ \\/ __ \\/ __ \\/ __ \\/ __ \\/ __/ / __/    / /|_/ / / ___/ ___/ __ \\");
        LOGGER.config(() ->" ___/ / / / / /_/ / /_/ / /_/ / /___/ /___   / /  / / / /__/ /  / /_/ /");
        LOGGER.config(() ->"/____/_/ /_/\\____/\\____/ .___/_____/_____/  /_/  /_/_/\\___/_/   \\____/ ");
        LOGGER.config(() -> "");
    }


    @PreDestroy
    private void stop() {
        LOGGER.severe("SnoopEE stopped");
    }
}
