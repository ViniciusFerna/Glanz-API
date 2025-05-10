package com.eventos.glanz.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eventos.glanz.provider.JWTProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityUserFilter extends OncePerRequestFilter{

	@Autowired
	private JWTProvider jwtProvider;
	
	//rotas permitidas sem autorização
	private static final List<String> PERMIT_LIST = List.of(
				"/api/user/"
			);
	
	// rotas privadas (com autenticação)
		private static final List<String> AUTH_LIST_STARTS_WITH = List.of(
				""
		);
		
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws IOException, ServletException {
			// Header -> Authorization -> "Bearer jgfd9ts5s490ksif0jsd874sfFJa"
			String header = request.getHeader("Authorization");
			String requestURI = request.getRequestURI();
			
			// Se a pessoa mandou token, entra no if
			if(header != null) {
				DecodedJWT token = jwtProvider.validateToken(header);
				
				if(token == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				
				// Se o token é valido
				request.setAttribute("user_id", token);
				
				// token de autenticação
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(token.getSubject(), null);
				
			}
			
			// Processa a requisição
			filterChain.doFilter(request, response);
		}
		
		// validar se é uma rota não protegida
		private boolean isNotProtectedEndpoint(String requestURI) {
			// lista de caminhos permitidos, verificar se é igual ou bate
			// -> /api/produtos => true
			// -> /api/user => false
			
			// o :: é uma forma simplificada de fazer isso
			// return PERMIT_LIST.stream().noneMatch(s -> requestURI.matches(s))
			return PERMIT_LIST.stream().noneMatch(requestURI::matches);
		}
		
		// validar se é uma rota protegida
		private boolean protectedList(String requestURI) {
			return AUTH_LIST_STARTS_WITH.stream().noneMatch(requestURI::startsWith);
		}
	
}
