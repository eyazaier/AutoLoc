package tn.esprit.autoloc.config;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* tn.esprit.services..*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Appel de la méthode {} avec les paramètres {}",
                joinPoint.getSignature().toShortString(),
                Arrays.deepToString(joinPoint.getArgs()));
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Fin d'exécution de la méthode {}", joinPoint.getSignature().toShortString());
    }
}
