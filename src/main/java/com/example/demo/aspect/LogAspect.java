package com.example.demo.aspect;

import com.example.demo.exception.AdviceException;
import com.example.demo.model.dto.TaskResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Before("@annotation(com.example.demo.aspect.annotation.LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before calling method: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "@annotation(com.example.demo.aspect.annotation.LogException)")
    public void logAfterThrowing(JoinPoint joinPoint) {
        log.info("Exception throwing in method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(com.example.demo.aspect.annotation.LogResult)",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, List<TaskResponseDto> result) {
        log.info("After returning method: " + joinPoint.getSignature().getName());
        log.info("Result: " + result);
    }

    @Around(value = "@annotation(com.example.demo.aspect.annotation.LogTracking)")
    public Object logAround(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object object;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            throw new AdviceException("Around exception");
        }
        long endTime = System.currentTimeMillis();
        log.info("Method {} working {} ms  ", joinPoint.getSignature().getName(), endTime - startTime);
        return object;
    }
}
