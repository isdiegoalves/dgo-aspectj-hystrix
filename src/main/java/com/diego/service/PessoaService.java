package com.diego.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.diego.entity.Pessoa;
import com.diego.repository.PessoaRepository;

public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Map<Long, Pessoa> cache = new HashMap<Long, Pessoa>();

	public Pessoa buscar(Long idPessoa) {
		
		Optional<Pessoa> pessoa = getCacheIfPresent(idPessoa);
		
		if (pessoa.isPresent()) {
			return pessoa.get();
		}
		
		PessoaRepository pessoaRepository = new PessoaRepository();
		
		Pessoa pessoaEncontrada = pessoaRepository.buscar(idPessoa);
		
		cache.put(idPessoa, pessoaEncontrada);
		
		return pessoaEncontrada;
	}

	private Optional<Pessoa> getCacheIfPresent(Long idPessoa) {

		return Optional.ofNullable(cache.get(idPessoa));
	}

}
