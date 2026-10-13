package com.recharge.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.recharge.service.RechargePlanService.*(..))")
    public void logBeforeServiceMethod() {
        System.out.println("AOP: Recharge service method called.");
    }
}
