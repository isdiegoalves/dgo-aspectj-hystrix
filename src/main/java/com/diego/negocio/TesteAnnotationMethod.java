package com.diego.negocio;

import com.diego.aop.auditoria.Auditavel;

public class TesteAnnotationMethod {

	@Auditavel
	public void teste() {

		System.out.print("TesteAnnotationMethod.xiu()");
	}
}
