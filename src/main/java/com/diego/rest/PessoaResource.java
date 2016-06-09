package com.diego.rest;

import java.io.Serializable;

import com.diego.aop.hystrixmetrics.HystrixWrapper;
import com.diego.aop.hystrixmetrics.HystrixWrapper.Mode;
import com.diego.entity.Pessoa;
import com.diego.service.PessoaService;

public class PessoaResource implements Serializable {

	private static final long serialVersionUID = 1L;

	@HystrixWrapper(commandGroupKey="PessoaResourceEndpoint", mode=Mode.ASYNC)
	public Pessoa buscar(Long idPessoa) {
		
		PessoaService pessoaService = new PessoaService();
		
		Pessoa pessoa = pessoaService.buscar(idPessoa);
		
		return pessoa;
	}

}
