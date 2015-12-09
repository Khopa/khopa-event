package com.khopa.spring.event.models;

import com.khopa.spring.event.EventManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Consumer {

    @Autowired
    @Setter @Getter
    protected EventManager eventManager;

    @PostConstruct
    public void postConstruct(){
        eventManager.subscribe(this);
    }

    @PreDestroy
    public void preDestroy(){
        eventManager.unsubscribe(this);
    }

}
