package com.desafiosefaz.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.desafiosefaz.models.Telefone;
import com.desafiosefaz.models.Usuario;

@Stateless
public class UsuarioDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Usuario> carregarTodosUsuarios() {

		return this.entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
	}

	public void deletar(Usuario usuario) {

		if (entityManager.contains(usuario)) {

			entityManager.remove(usuario);

		} else {

			Usuario usuarioDeletado = entityManager.find(Usuario.class, usuario.getId());

			if (usuarioDeletado != null) {
				entityManager.remove(usuarioDeletado);
			}
		}
	}

	public void adicionarNovoUsuario(Usuario usuario, List<Telefone> telefones) {

		this.entityManager.persist(usuario);

		for (int i = 0; i < telefones.size(); ++i) {

			Telefone telefone = (Telefone) telefones.get(i);
			telefone.setUsuario(usuario);

			this.entityManager.persist(telefone);
		}
	}

	public void alterar(List<Usuario> usuario) {
		usuario.forEach(entityManager::merge);
	}

	public Usuario buscarLogin(String email, String senha) {

		try {

			Usuario usuario = (Usuario) entityManager
					.createQuery("SELECT u from Usuario u where u.email = :email and u.senha = :senha")
					.setParameter("email", email).setParameter("senha", senha).getSingleResult();

			return usuario;

		} catch (NoResultException e) {
			return null;
		}
	}

	public void inserirTelefone(List<Telefone> telefones, Usuario usuario) {

		for (int i = 0; i < telefones.size(); ++i) {

			Telefone telefone = (Telefone) telefones.get(i);
			telefone.setUsuario(usuario);

			this.entityManager.persist(telefone);
		}
	}

	public List<Telefone> buscarTelefonesUsuario(Usuario usuario) {

		return this.entityManager
				.createQuery("SELECT u from Telefone u where u.usuario.id = :usuarioId", Telefone.class)
				.setParameter("usuarioId", usuario.getId()).getResultList();
	}

	public void deletarTelefone(Telefone telefone, Usuario usuario) {

		Query query = entityManager.createQuery("delete Telefone u where u.usuario.id = :usuarioId and u.id = :id");
		query.setParameter("usuarioId", usuario.getId()).setParameter("id", telefone.getId()).executeUpdate();
	}

	public void alterarTelefone(List<Telefone> telefone) {

		telefone.forEach(entityManager::merge);
	}

	public void adicionarNovoTelefone(Usuario usuario, Telefone telefone) {

		Telefone newTelefone = new Telefone(telefone.getDdd(), telefone.getNumero(), telefone.getTipoTelefone(),
				usuario);
		this.entityManager.persist(newTelefone);
	}
}
