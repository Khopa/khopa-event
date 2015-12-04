package com.khopa.spring.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotated with this annotation are supposedly event consumer.
 *
 *
 * Applying COC (Convention over Configuration) philosophy :
 * - Method should be named 'on' + EventName
 * - Be of type boolean
 *
 * Example :
 *
 * @Consume
 * public boolean onSomeEvent(String ... args){
 *     // do stuff
 * }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Consume {
    String event() default "";
    int max() default -1;
    int priority() default 0;
}
