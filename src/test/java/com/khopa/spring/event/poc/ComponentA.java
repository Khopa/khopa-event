package com.khopa.spring.event.poc;

import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.models.Consumer;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class ComponentA extends Consumer {

    @Getter int a = 0;
    @Getter int b = 0;

    @Consume(max = 1)
    public boolean onEventA(){
        log.debug("A - A");
        return true;
    }

    @Consume(max = 1, event = "EventA")
    public boolean onEventA2(){
        log.debug("A - A2");
        return true;
    }

    @Consume(event = "evt_125634")
    public boolean onEventSpecialName(){
        log.debug("A - evt_125634");
        return false;
    }

    @Consume
    public boolean onEventB(){
        log.debug("A - B");
        return false;
    }

}
