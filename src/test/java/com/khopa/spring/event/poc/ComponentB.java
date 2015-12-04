package com.khopa.spring.event.poc;

import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.annotations.ProduceAfter;
import com.khopa.spring.event.annotations.ProduceBefore;
import com.khopa.spring.event.models.Consumer;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
public class ComponentB extends Consumer{

    @Consume
    public boolean onEventB(){
        System.out.println("B - B");
        eventManager.fire("EventD");
        return false;
    }

    @Consume
    public boolean onEventC(){
        System.out.println("B - C");
        eventManager.fire("EventD");
        return false;
    }

    @Consume
    @ProduceAfter
    public boolean onEventD(){
        System.out.println("B - D");
        return false;
    }

    @Consume(event = "evt_274896")
    @ProduceBefore
    public boolean onSomeModelCreated(SomeModel someModel){
        log.info("Some model created");
        log.info(someModel.getName());
        log.info(someModel.getA());
        log.info(someModel.getB());
        return false;
    }

}
