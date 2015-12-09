package com.khopa.spring.event.impl;

import com.khopa.spring.event.models.Subscription;
import lombok.extern.apachecommons.CommonsLog;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread Pooled Event Manager implementation execute process each event on a separate thread.
 */
@CommonsLog
public class ThreadPooledEventManager extends SimpleEventManager {

    /**
     * Number of threads in the pool
     */
    protected int threadCount = 25;

    /**
     * Thread pool executor
     */
    private ExecutorService executorService;

    /**
     * Construct a ThreadPooledEventManager with a fixed count of threads.
     */
    public ThreadPooledEventManager(int threadCount){
        super();
        this.threadCount = threadCount;
        executorService = Executors.newFixedThreadPool(threadCount);
        log.debug(String.format("Initialized ThreadPooledEventManager with %d threads.", threadCount));
    }

    /**
     * Default constructor (10 Threads)
     */
    public ThreadPooledEventManager(){
        this(10);
    }

    @Override
    public void fire(String event, Object... params) {
        executorService.submit(new EventThread(event, params));
    }

    /**
     * Separate Thread class processing events
     */
    private class EventThread implements Runnable{

        private String event;
        private Object[] params;

        public EventThread(String event, Object... params){
            this.event = event;
            this.params = params;
        }

        @Override
        public void run() {
            ThreadPooledEventManager.super.fire(event, params);
        }

    }

}
