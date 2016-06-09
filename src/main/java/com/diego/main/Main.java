package com.diego.main;

import com.diego.aop.temporizacao.Temporizavel;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import rx.Observable;
import rx.Subscriber;

public class Main {

	@Temporizavel
	public static void main(String[] args) throws InterruptedException {
		
		Setter setter = HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("observer-teste"))
			    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
			    	.withExecutionTimeoutInMilliseconds(1250));

		HystrixCommand<String> command = new HystrixCommand<String>(setter) {

			@Override
			protected String run() throws Exception {
				System.out.println("Main.main(...).new HystrixCommand() {...}.run()");
				return "semente gerada";
			}
			
		};
		
		Observable<String> obsever = command.observe();
		obsever.subscribe(new Subscriber<String>() {

			@Override
			public void onCompleted() {
				System.out.println("completado");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("deu erro porra");
			}

			@Override
			public void onNext(String t) {
				System.out.println("cheguei");
			}
			
		});
		
		String resultado = command.execute();
		
		System.out.println("resultado "+ resultado);
		/*

		for (int i = 0; i < 1000; i++) {
			
			PessoaResource pessoaResource = new PessoaResource();
			
			Long idPessoa = 1 + (long) (Math.random() * 15);
			Pessoa pessoa = pessoaResource.buscar(idPessoa);
			
			System.out.println();
		}
		
		System.out.println("terminou");*/
	}
}
