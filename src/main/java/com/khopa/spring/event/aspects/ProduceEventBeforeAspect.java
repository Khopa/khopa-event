package com.khopa.spring.event.aspects;

import com.khopa.spring.event.annotations.ProduceBefore;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@Component
@Aspect
@CommonsLog
public class ProduceEventBeforeAspect {

    /*@Before(value = "@annotation(ProduceBefore)", argNames="joinPoint")
    public void before(JoinPoint joinPoint, ProduceBefore produceBefore) throws Throwable {
        log.info("before, class: " + joinPoint.getSignature().getDeclaringType().getSimpleName() + ", method: " + joinPoint.getSignature().getName());
    }*/

}
