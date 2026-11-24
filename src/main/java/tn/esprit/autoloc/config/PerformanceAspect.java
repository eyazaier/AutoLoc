package tn.esprit.autoloc.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("execution(* tn.esprit.services..*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            String method = joinPoint.getSignature().toShortString();
            log.info("Temps d'exécution de la méthode {} : {} ms", method, duration);
            if (duration > 1000) {
                log.warn("Méthode lente détectée : {} ({} ms)", method, duration);
            }
        }
    }
}
