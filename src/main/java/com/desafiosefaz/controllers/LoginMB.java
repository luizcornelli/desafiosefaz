package com.desafiosefaz.controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.desafiosefaz.dao.UsuarioDAO;
import com.desafiosefaz.models.Usuario;

@Named
@SessionScoped
public class LoginMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuarioLogado;
	private String email;
	private String senha;

	@Inject
	private UsuarioDAO usuarioDAO;

	public LoginMB() {

	}

	public String logIn() {

		usuarioLogado = usuarioDAO.buscarLogin(email, senha);

		if (usuarioLogado == null) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou Senha Inválidos", "Login Inválido"));
			return null;

		} else {

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (session != null) {
				session.setAttribute("usuario", usuarioLogado);
			}
			return "/sistema/home?faces-redirect=true";
		}
	}

	public String logOff() {

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.invalidate();

		return "/index?faces-redirect=true";
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
}
