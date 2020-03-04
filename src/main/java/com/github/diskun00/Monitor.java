package com.github.diskun00;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * monitor email sending status
 */
public class Monitor extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Monitor.class);

    /**
     * number of sent emails
     */
    public static final AtomicLong SENT_COUNT = new AtomicLong(0);

    /**
     * monitor frequency (time unit: second)
     */
    private static final int MONITOR_FREQUENCY = 2;

    public void run() {
        while(true){
            LOGGER.info(SENT_COUNT+ " emails have been sent.");
            try {
                TimeUnit.SECONDS.sleep(MONITOR_FREQUENCY);
            } catch (InterruptedException e) {
                LOGGER.trace(e);
            }
        }
    }
}
