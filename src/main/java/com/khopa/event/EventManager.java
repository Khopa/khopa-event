package com.khopa.event;

import org.springframework.stereotype.Component;

@Component
public interface EventManager {

    /**
     * Fire an event on the bus
     * @param event Type of event to fire
     * @param params Params for the event
     */
    void fire(String event, Object... params);

    /**
     * Automatically subscribe an object on the bus (All Consume methods are bound on the bus)
     * @param subscriber Object needing to subscribe
     */
    void subscribe(Object subscriber);

    /**
     * Automatically unsubscribe an object on the bus (All Consume methods are removed from the bus)
     * @param subscriber Object needing unsubscription
     */
    void unsubscribe(Object subscriber);

}
