package com.github.diskun00;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    /**
     * number of emails to send for simulation
     */
    public static final long NUM_EMAILS = 200L;
    /**
     * email queue size
     */
    private static final int QUEUE_SIZE = 1000;

    /**
     * thread pool size / number of email senders
     */
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        LinkedBlockingDeque<EmailBean> emailsBlockingDeque = new LinkedBlockingDeque<>(QUEUE_SIZE);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        EmailListReader emailListReader = new EmailListReader(emailsBlockingDeque, countDownLatch);
        new Thread(emailListReader).start();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        EmailSender emailSender = new EmailSender(emailsBlockingDeque, countDownLatch);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(emailSender);
        }

        //monitor thread to check sending status
        Monitor monitor = new Monitor();
        monitor.setDaemon(true);
        monitor.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.trace(e);
        }

        LOGGER.info("All emails are read. Shut down executor service.");
        executorService.shutdown();
    }
}
