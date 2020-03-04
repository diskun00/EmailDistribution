package com.github.diskun00;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * email list retrieval
 */
public class EmailListReader implements Runnable {

    private BlockingQueue<EmailBean> blockingQueue;
    private CountDownLatch countDownLatch;
    private static final Logger LOGGER = Logger.getLogger(EmailListReader.class);


    public EmailListReader(BlockingQueue<EmailBean> blockingQueue, CountDownLatch countDownLatch) {
        this.blockingQueue = blockingQueue;
        this.countDownLatch = countDownLatch;
    }

    /**
     * generate NUM_EMAILS emails to queue
     *
     */
    @Override
    public void run() {
        for (int i = 0; i <= App.NUM_EMAILS; i++) {
            EmailBean emailBean = new EmailBean("user" + i + "@hallo.ahoj", "user" + i);
            try {
                LOGGER.debug("New email read :" + emailBean.getEmailAddress());
                blockingQueue.put(emailBean);
            } catch (InterruptedException e) {
                LOGGER.trace("add email error", e);
            }
        }
        countDownLatch.countDown();
    }
}