package com.diego.aop.temporizacao;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TempoExecucaoAspect {

	@Around("@annotation(com.diego.aop.temporizacao.Temporizavel) && execution(* *(..))")
	public Object calcularExecucao(ProceedingJoinPoint joinPoint) {

		Instant first = Instant.now();
		System.out.printf("cronometro iniciado...%n");
		Object retorno = null;
		try { retorno = joinPoint.proceed(); } catch (Throwable ignore) {}
		Duration duration = Duration.between(first, Instant.now());
		System.out.printf("parado..executado em %s milissegundos%n", duration.toMillis());
		return retorno;
	}
	
}
