package com.khopa.spring.event.poc;

import com.khopa.spring.event.annotations.Consume;
import com.khopa.spring.event.models.Consumer;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
public class ComponentB extends Consumer{

    @Consume
    public boolean onEventB(){
        log.debug("B - B");
        eventManager.fire("EventD");
        return false;
    }

    @Consume
    public boolean onEventC(){
        log.debug("B - C");
        eventManager.fire("EventD");
        return false;
    }

    @Consume
    public boolean onEventD(){
        log.debug("B - D");
        return false;
    }

    @Consume(event = "evt_274896")
    public boolean onSomeModelData(SomeModel someModel){
        log.info("Some model data");
        log.info(someModel.getName());
        log.info(someModel.getA());
        log.info(someModel.getB());
        return false;
    }

}
