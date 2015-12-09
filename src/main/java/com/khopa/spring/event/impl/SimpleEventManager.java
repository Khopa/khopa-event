package com.khopa.spring.event.impl;

import com.khopa.spring.event.EventManager;
import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.models.Subscription;
import lombok.Synchronized;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The most straightforward Event Manager implementation
 * Process events synchronously on the same thread as your main application.
 */
@CommonsLog
public class SimpleEventManager implements EventManager {

    /**
     * Data structures for subscriptions
     */
    private ConcurrentHashMap<String, List<Subscription>> subscribers;

    public SimpleEventManager(){
        subscribers = new ConcurrentHashMap<>();
    }

    /**
     * @see EventManager
     * @param event Type of event to fire
     * @param params Params for the event
     */
    public void fire(String event, Object... params) {
        List<Subscription> subscriptions = subscribers.get(event.toLowerCase());
        List<Subscription> expired =  new ArrayList<Subscription>();
        if(subscriptions == null) return;
        for(Subscription subscription: subscriptions){
            Object result = null;
            try {
                result = executeSubscription(subscription, params);
                subscription.setMaxConsumption(subscription.getMaxConsumption()-1);
                if(subscription.getMaxConsumption() == 0){
                    expired.add(subscription);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e){
                log.warn(event + " invalid number of args on event invokation. Aborting.");
            }
            if(result != null && result instanceof Boolean){
                if((Boolean) result){
                    log.info(event + " consumed");
                    break;
                }
            }
        }
        subscriptions.removeAll(expired);
    }

    /**
     * Execute a subscription
     * @param subscription Subscription to execute
     * @param params Params for the subscribing method if provided
     * @return Whatever is returned by the subscribing method
     * @throws InvocationTargetException If the subscribing method isn't available anymore
     * @throws IllegalAccessException If the subscribing method can't be accessed
     * @throws IllegalArgumentException If the number of args provided doesn't match the subscribing method
     */
    protected Object executeSubscription(Subscription subscription, Object... params) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        return subscription.getConsumer().invoke(subscription.getSubscriber(), params);
    }

    /**
     * @see EventManager
     * @param subscriber Object needing to subscribe
     */
    public void subscribe(Object subscriber) {
        Method[] candidateMethods = subscriber.getClass().getDeclaredMethods();
        List<Method> subscribingMethod = new ArrayList<Method>();
        for(Method consumer: candidateMethods){
            if(consumer.isAnnotationPresent(Consume.class)){
                Consume consume = consumer.getAnnotation(Consume.class);
                Subscription subscription = new Subscription();
                subscription.setSubscriber(subscriber);
                subscription.setMaxConsumption(consume.max());
                subscription.setConsumer(consumer);
                subscription.setPriority(consume.priority());

                String event = consume.event();
                if(event.equals("")){
                    event = consumer.getName();
                    if(event.startsWith("on")){
                        event = consumer.getName().substring(2);
                    }
                }
                if(event.length() > 0){
                    registerSubscription(event, subscription);
                }
            }
        }
    }

    /**
     * @see EventManager
     * @param subscriber Object needing unsubscription
     */
    public void unsubscribe(Object subscriber) {
        for(String key: subscribers.keySet()){
            List<Subscription> currentList = subscribers.get(key);
            List<Subscription> toBeDeleted = new ArrayList<Subscription>();
            for(Subscription subscription: currentList){
                if(subscription.getSubscriber()==subscriber){
                    toBeDeleted.add(subscription);
                }
            }
            currentList.removeAll(toBeDeleted);
        }
    }

    /**
     * Register a new subscription
     * @param event Event type to subscribe to
     * @param subscription Subscription object containing subscribing object and method and other subscription parameters.
     */
    private void registerSubscription(String event, Subscription subscription){
        event = event.toLowerCase();
        if(!subscribers.containsKey(event)){
            List<Subscription> subscriptions = new ArrayList<Subscription>();
            subscriptions.add(subscription);
            subscribers.put(event, subscriptions);
        }else{
            subscribers.get(event).add(subscription);
        }
    }

}
