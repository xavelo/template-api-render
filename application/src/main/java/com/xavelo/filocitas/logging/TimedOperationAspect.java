package com.xavelo.filocitas.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class TimedOperationAspect {

    private static final Logger log = LoggerFactory.getLogger(TimedOperationAspect.class);

    @Around("@annotation(com.xavelo.filocitas.logging.TimedOperation) && @annotation(timedOperation)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, TimedOperation timedOperation) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            if (log.isDebugEnabled()) {
                log.debug("{} took {} ms", resolveOperationName(joinPoint, timedOperation), durationMs);
            }
        }
    }

    private String resolveOperationName(ProceedingJoinPoint joinPoint, TimedOperation timedOperation) {
        String configuredName = timedOperation.value();
        if (configuredName != null && !configuredName.isBlank()) {
            return configuredName;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }
}
