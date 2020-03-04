package com.github.diskun00;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * email sender
 */
public class EmailSender implements Runnable {

    private BlockingQueue<EmailBean> blockingQueue;
    private CountDownLatch countDownLatch;
    private static final Logger LOGGER = Logger.getLogger(EmailListReader.class);

    public EmailSender(BlockingQueue<EmailBean> blockingQueue, CountDownLatch countDownLatch) {
        this.blockingQueue = blockingQueue;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        //flag to stop the thread
        boolean stop = false;
        while (!stop) {
            try {
                // take an email and send
                EmailBean emailBean = null;
                while (emailBean == null){
                    //poll emails from the queue as long as it is not empty
                    emailBean = blockingQueue.poll(1, TimeUnit.SECONDS);
                    if (emailBean == null) {
                        if(countDownLatch.getCount() == 0){
                            //shut email sender thread if EmailListReader thread is off and no data in queue
                            stop = true;
                            break;
                        }
                        TimeUnit.SECONDS.sleep(1);
                    }
                }
                if(stop){
                    //exit the loop
                    LOGGER.debug("Stop emailSender thread," + Thread.currentThread().getName());
                    break;
                }
                LOGGER.debug("sending email: " + emailBean.getEmailAddress());
                TimeUnit.MILLISECONDS.sleep(500);
                Monitor.SENT_COUNT.incrementAndGet();
                LOGGER.debug("sent email: " + emailBean.getEmailAddress());
            } catch (InterruptedException e) {
                LOGGER.trace("Exception when sending emails ", e);
            }
        }
    }
}