package com.khopa.spring.event.poc;

import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.models.Consumer;
import lombok.Getter;

public class ComponentA extends Consumer {

    @Getter int a = 0;
    @Getter int b = 0;

    @Consume(max = 1)
    public boolean onEventA(){
        System.out.println("A - A");
        return true;
    }

    @Consume(max = 1, event = "EventA")
    public boolean onEventA2(){
        System.out.println("A - A2");
        return true;
    }

    @Consume(event = "evt_125634")
    public boolean onEventSpecialName(){
        System.out.println("A - evt_125634");
        return false;
    }

    @Consume
    public boolean onEventB(){
        System.out.println("A - B");
        return false;
    }

}
