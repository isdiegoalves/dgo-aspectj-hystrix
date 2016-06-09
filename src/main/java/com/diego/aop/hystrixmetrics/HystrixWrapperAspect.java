package com.diego.aop.hystrixmetrics;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.diego.aop.hystrixmetrics.HystrixWrapper.Mode;
import com.diego.entity.Pessoa;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

@Aspect
public class HystrixWrapperAspect {

	 @Pointcut("within(com.diego..*) && @annotation(com.diego.aop.hystrixmetrics.HystrixWrapper)")
	 public void hystrixAspectPointcut() {}
	
	@Around("hystrixAspectPointcut()")
	public Object around(final ProceedingJoinPoint joinPoint) throws InterruptedException, ExecutionException {

		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		HystrixWrapper annotation = method.getAnnotation(HystrixWrapper.class);
		if (annotation == null)
			throw new IllegalStateException();
		

		String commandGroupKey = annotation.commandGroupKey();
		Mode mode = annotation.mode();

		System.out.println("Executando a operação com HystrixWrapper Mode[" + mode + "]");
		
		
		Setter setter = HystrixCommand.Setter
			.withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroupKey))
		    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		    	.withExecutionTimeoutInMilliseconds(1250));
		
		HystrixCommand<Object> command = new HystrixCommand<Object>(setter) {

			@Override
			protected Object run() throws Exception {
				try {
					return joinPoint.proceed();
				} catch (Exception e) {
					throw e;
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			protected Object getFallback() {
				System.out.println("Circuito Aberto >>> Fallback! " + getMetrics().getHealthCounts());
				return new Pessoa(0L, "Fallback");
			}


		};
		
		return Mode.ASYNC.equals(mode) ? 
				command.queue() : 
					command.execute();
	}

}