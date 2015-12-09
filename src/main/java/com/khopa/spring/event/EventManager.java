package com.khopa.spring.event;

import org.springframework.stereotype.Component;

@Component
public interface EventManager {

    void fire(String event, Object... params);

    void subscribe(Object subscriber);

    void unsubscribe(Object subscriber);

}
