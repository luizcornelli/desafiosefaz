package com.desafiosefaz.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.desafiosefaz.dao.UsuarioDAO;
import com.desafiosefaz.models.Telefone;
import com.desafiosefaz.models.Usuario;

@Named
@SessionScoped
public class UsuarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Usuario> usuarios;

	private Usuario usuario = new Usuario();

	private List<Telefone> telefones = new ArrayList<>();

	private Telefone telefone = new Telefone();

	@Inject
	private UsuarioDAO usuarioDAO;

	@PostConstruct
	public void init() {

		this.usuarios = usuarioDAO.carregarTodosUsuarios();
	}

	public void deletar(Usuario usuario) {

		usuarioDAO.deletar(usuario);
		usuarios.remove(usuario);
	}

	public String adicionar() {

		usuarioDAO.adicionarNovoUsuario(usuario, telefones);

		this.telefones.clear();
		this.usuarios = usuarioDAO.carregarTodosUsuarios();
		this.usuario = new Usuario();
		this.telefones.clear();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Cadastro efetuado com sucesso.");
		PrimeFaces.current().dialog().showMessageDynamic(message);

		return "home";
	}

	public void alterar() {
		
		System.out.println(this.usuario.getId()); 
		usuarioDAO.alterar(usuarios);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Registro(s) alterado(s) com sucesso.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void adicionarTelefone() {

		this.telefones.add(telefone);
		this.telefone = new Telefone();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
				"Telefone foi incrementado na sua lista.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public String chamaPaginaCadastroUsuario() {

		return "cadastroUsuario";
	}

	public String chamaPaginaInserirAlterarTelefone(Usuario usuario) {

		this.telefones = usuarioDAO.buscarTelefonesUsuario(usuario);
		this.usuario = usuario;

		return "alterarTelefoneUsuario";
	}

	public String voltarParaTelaIncial() {

		this.usuario = new Usuario();
		this.telefones = new ArrayList<>();

		return "home";
	}

	public void deletarTelefone(Telefone telefone) {

		usuarioDAO.deletarTelefone(telefone, usuario);
		telefones.remove(telefone);
	}

	public void alterarTelefone() {

		usuarioDAO.alterarTelefone(telefones);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
				"Telefone foi alterado com sucesso.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void adicionarNovoTelefone() {

		usuarioDAO.adicionarNovoTelefone(usuario, telefone);

		this.telefones.add(telefone);
		this.telefone = new Telefone();

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
				"Telefone foi incrementado na sua lista");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void deletarTelefoneFormulario(Telefone telefone) {

		this.telefones.remove(telefone);
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}
}
