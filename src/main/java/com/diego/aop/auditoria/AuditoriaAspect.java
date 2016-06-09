package com.diego.aop.auditoria;

import static java.lang.String.format;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class AuditoriaAspect {

    @Pointcut("execution(* *(..)) && "
    		//+ " @annotation(com.diego.aop.auditoria.Auditavel) &&"
    		+ "("
    		+ "within(com.diego.repository.*Repository) || "
    		+ "within(com.diego.service.*Service) || "
    		+ "within(com.diego.rest.*Resource)"
    		+ ")")
    public void loggingPointcut() {}
    
    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    	
    	System.out.println(format("Exceção em %s.%s() causada por = %s erro %s", 
    			joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(), e.getCause(), e));
    }

    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {

    		System.out.println(format("Entrou: %s.%s() com argumentos = %s", 
    				joinPoint.getSignature().getDeclaringType().getSimpleName(), 
    				joinPoint.getSignature().getName(),
    				Arrays.toString(joinPoint.getArgs())));
    	}
    	
        try {
           
        	Object result = joinPoint.proceed();
            
        	System.out.println(format("Saindo: %s.%s() com resultado = %s", 
        			joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(), result));
        	
            return result;
            
        } catch (IllegalArgumentException e) {
        	
        	System.out.println(format("Illegal argument: %s in %s.%s()", 
        			Arrays.toString(joinPoint.getArgs()),
        			joinPoint.getSignature().getDeclaringType().getSimpleName(), 
        			joinPoint.getSignature().getName()));

            throw e;
        }
    }
}
