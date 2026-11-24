package tn.esprit.autoloc.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    @Pointcut("execution(* tn.esprit.services..*(..))")
    public void serviceMethods() {
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        log.error("Exception dans la méthode {} : type={}, message={}",
                joinPoint.getSignature().toShortString(),
                ex.getClass().getName(),
                ex.getMessage());
    }
}
