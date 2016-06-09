package com.diego.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.diego.entity.Pessoa;

public class PessoaRepository {

	public Pessoa buscar(Long idPessoa) {

		Long timeout = 1 + (long) (Math.random() * 2000);
		try { TimeUnit.MILLISECONDS.sleep(timeout); } catch (InterruptedException ignore) {}
		
		return new Pessoa(idPessoa, UUID.randomUUID().toString());
	}

}
