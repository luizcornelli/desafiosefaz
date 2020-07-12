package com.desafiosefaz.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.desafiosefaz.controllers.LoginMB;

@WebFilter(urlPatterns = "/sistema/*")
public class ControlleAcessoFilter implements Filter {

	@Inject
	protected LoginMB loginMB;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (loginMB.getUsuarioLogado() == null) {

			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendRedirect("/desafiosefaz/index.xhtml");
		}
		chain.doFilter(request, response);
	}
}
