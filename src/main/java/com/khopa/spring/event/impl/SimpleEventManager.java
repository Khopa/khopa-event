package com.khopa.spring.event.impl;

import com.khopa.spring.event.EventManager;
import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.models.Subscription;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CommonsLog
public class SimpleEventManager implements EventManager {

    private HashMap<String, List<Subscription>> subscribers;

    @PostConstruct
    public void init(){
        subscribers = new HashMap<String, List<Subscription>>();
    }

    public void fire(String event) {
        System.out.println("FIRE " +event);
        List<Subscription> subscriptions = subscribers.get(event.toLowerCase());
        List<Subscription> expired =  new ArrayList<Subscription>();
        if(subscriptions == null) return;
        for(Subscription subscription: subscriptions){
            try {
                Object result = subscription.getConsumer().invoke(subscription.getSubscriber());
                subscription.setMaxConsumption(subscription.getMaxConsumption()-1);
                if(subscription.getMaxConsumption() == 0){
                    expired.add(subscription);
                }
                if(result instanceof Boolean){
                    if((Boolean) result){
                        log.info(event + " consumed");
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        subscriptions.removeAll(expired);
    }

    public void fire(String event, Object... params) {
        System.out.println("FIRE " +event);
        List<Subscription> subscriptions = subscribers.get(event.toLowerCase());
        List<Subscription> expired =  new ArrayList<Subscription>();
        if(subscriptions == null) return;
        for(Subscription subscription: subscriptions){
            try {
                Object result = subscription.getConsumer().invoke(subscription.getSubscriber(), params);
                subscription.setMaxConsumption(subscription.getMaxConsumption()-1);
                if(subscription.getMaxConsumption() == 0){
                    expired.add(subscription);
                }
                if(result instanceof Boolean){
                    if((Boolean) result){
                        log.info(event + " consumed");
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e){
                log.warn(event + " unvalid number of args on event invokation.");
            }
        }
        subscriptions.removeAll(expired);
    }

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
