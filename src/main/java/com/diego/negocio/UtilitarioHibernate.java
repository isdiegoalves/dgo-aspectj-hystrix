package com.diego.negocio;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.diego.aop.auditoria.Auditavel;
import com.diego.aop.temporizacao.Temporizavel;

public class UtilitarioHibernate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Auditavel
	@Temporizavel
	public int teste() {
		int timeout = 2 + (int)(Math.random() * 15);
		try {
			TimeUnit.SECONDS.sleep(timeout );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return timeout;
	}
}
