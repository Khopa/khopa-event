khopa-event
===========

Simple & easy to use event manager for java/spring. 

How to use :
------------

**Step 1 - Add an event manager to your application context**

```
<bean id="eventManager" class="com.khopa.event.impl.SimpleEventManager"/>
```

**Step 2 - Subscribe to event**

```
@Component public class SomeComponent{

    @Autowired private EventManager eventManager;

    @PostConstruct public void init(){
        eventManager.subscribe(this);
    }

    /**
     * This method will be called when the event "someEvent" is fired
     */
    @Consume public boolean onSomeEvent(){
        log.debug("on some event");
        return false;
    }
    
    /**
     * This method will be called when the event "someEvent" is fired
     */
    @Consume public boolean onSomeEventWithParams(String p1, String p2, int p3){
        log.debug("on some event with params");
        return false;
    }

}

```

Or extends consumer class to automatically subscribe on the event manager.

```
/**
 * Equivalent to the previous code
 */
@Component public class SomeComponent extends Consumer{

    @Consume public boolean onSomeEvent(){
        log.debug("on some event");
        return false;
    }
    
    /**
     * This method will be called when the event "someEvent" is fired
     */
    @Consume public boolean onSomeEventWithParams(String p1, String p2, int p3){
        log.debug("on some event with params");
        return false;
    }

}
```

**Step 3 - Fire events from anywhere**

```
// Fire an event without parameters
eventManager.fire("someEvent")

// Fire with varargs parameters
eventManager.fire("someEventWithParams", "param1", "param2", 42)
```

That's for the gist of it.

(Not so) Advanced features :
----------------------------

**Run events on a separate thread pool**

Use the ThreadPooledEventManager as your EventManager if you want to dispatch event processing on separate threads.

```
<bean id="eventManager" class="com.khopa.event.impl.ThreadPooledEventManager">
    <constructor-arg name="threadCount" value="8"/> <!-- Setup here the number of thread you need in your pool -->
</bean>
```

**Another event routing option**

By default the subscribing method name is used to determine the event bound to it. However you can also force the event name manually as a parameter in the Consume annotation.

```
@Consume(event = "イベント")
public boolean thisMethodNameIsNotRelevant(int dontForgetParamsAreAllowed){
    log.info("イベント called : " + String.valueOf(dontForgetParamsAreAllowed);
    return false;
}
```

Will be triggered if you call :

```
eventManager.fire("イベント", 13)
```

Yes, it may be cool to support regexes and/or routing parameters in future iterations.

**Limiting events received**

The @Consume annotation can take a parameter to limit the number of event that can be received :

```
// This method will be called at most 10 time per subscribing instance.
@Consume(max = 10)
public boolean onEventA(){
    log.debug("A - A");
    return true;
}
```

**Consuming an event**

If the consume method return a boolean, returning true will stop event propagation to others subscribers.

```
@Consume public boolean onSomeEventWithParams(String p1, String p2, int p3){
    log.debug("on some event with params");
    return true; // No other subscribers will receive the event after this consumer.
}
```

There is currently no priority system so this is kinda useless/random but as this may exists as a feature in the future, this feature is currently implemented as is.











