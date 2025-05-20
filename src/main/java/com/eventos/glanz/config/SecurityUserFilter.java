package com.eventos.glanz.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
		
		private String getTokenFromHeader(HttpServletRequest request) {
			String header = request.getHeader("Authorization");
			
			if (header == null || !header.startsWith("Bearer ")) {
				return null;
			}
			
			return header.replace("Bearer ", "").trim();		
			}
		
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws IOException, ServletException {
			String token = getTokenFromHeader(request);
			
			DecodedJWT decodedJWT = jwtProvider.validateToken(token);
			if(token == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Inválido");
				return;
			}
			
			String role = decodedJWT.getClaim("roles").asString();
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					decodedJWT.getSubject(),
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
				);
			
			SecurityContextHolder.getContext().setAuthentication(auth);
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
