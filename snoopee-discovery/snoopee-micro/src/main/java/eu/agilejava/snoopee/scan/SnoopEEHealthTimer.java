/*
 * The MIT License
 *
 * Copyright 2016 Ivar Grimstad (ivar.grimstad@gmail.com).
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

import eu.agilejava.snoopee.client.SnoopEEConfig;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 *
 * @author Ivar Grimstad (ivar.grimstad@gmail.com)
 */
//@ApplicationScoped
public class SnoopEEHealthTimer {
    
    private static final Logger LOGGER = Logger.getLogger("eu.agilejava.snoopee");
    
    private Timer timer;
    
    
    private static final class HealthPing extends TimerTask {
        
        @Override
        public void run() {
            LOGGER.config(() -> "I am healthy!");
//            snoopEEClient.health();
        }
    }
   
    public void init(SnoopEEConfig configEvent) {
        
        LOGGER.config("EVENT");
        TimerTask health = new HealthPing();
        timer = new Timer();
        timer.scheduleAtFixedRate(health, 0, 10000);
    }
}
