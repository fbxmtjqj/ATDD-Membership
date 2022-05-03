package com.fbxmtjqj.tdd.membership.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Log4j2
@Aspect
@Component
public class ExecutionTimeAop {

    @Pointcut("within(com.fbxmtjqj.tdd.membership.app.membership.controller..*)")
    public void ExecutionTimeChecker(){ }

    @Around("ExecutionTimeChecker()")
    public Object calculateExecutionTime(final ProceedingJoinPoint pjp) throws Throwable {
        // 해당 클래스 처리 전의 시간
        final StopWatch sw = new StopWatch();
        sw.start();

        // 해당 클래스의 메소드 실행
        final Object result = pjp.proceed();

        // 해당 클래스 처리 후의 시간
        sw.stop();
        final long executionTime = sw.getTotalTimeMillis();

        final String className = pjp.getTarget().getClass().getName();
        final String methodName = pjp.getSignature().getName();
        final String task = className + "." + methodName;

        log.info("[ExecutionTime] " + task + "-->" + executionTime + "(ms)");

        return result;
    }

}