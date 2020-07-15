package com.desafiosefaz.services;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.desafiosefaz.models.Telefone;
import com.desafiosefaz.models.Usuario;

@Startup
@Singleton
public class DBCarga {

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	public void init() {

		System.out.println("Inicializando carga inicial dos Usuários...");

		Usuario usuario0 = new Usuario("admin", "admin@admin.com", "12345", true); 
		
		Usuario usuario1 = new Usuario("Luiz", "luiz@gmail.com", "12345", false);
		Usuario usuario2 = new Usuario("Maria", "maria@hotmail.com", "54321", false);
		Usuario usuario3 = new Usuario("José", "jose@bol.com.br", "23415", false);

		Telefone telefone1 = new Telefone("81", "888888888", "Residencial", usuario1);
		Telefone telefone2 = new Telefone("22", "333444345", "Pessoal", usuario2);
		Telefone telefone3 = new Telefone("72", "333222112", "Trabalho", usuario3);

		usuario1.getTelefones().addAll(Arrays.asList(telefone1));
		usuario2.getTelefones().addAll(Arrays.asList(telefone2));
		usuario3.getTelefones().addAll(Arrays.asList(telefone3));
		
		this.entityManager.persist(usuario0);
		this.entityManager.persist(usuario1);
		this.entityManager.persist(usuario2);
		this.entityManager.persist(usuario3);

		this.entityManager.persist(telefone1);
		this.entityManager.persist(telefone2);
		this.entityManager.persist(telefone3);
	}
}
