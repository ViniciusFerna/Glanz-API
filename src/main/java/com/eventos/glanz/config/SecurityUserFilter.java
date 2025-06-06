package com.eventos.glanz.config;

import java.io.IOException;
import java.util.Collections;

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
	
		
		private String getTokenFromHeader(HttpServletRequest request) {
			String header = request.getHeader("Authorization");
            System.out.println("DEBUG: Looking for Authorization header..."); // DEBUG
			if (header == null || !header.startsWith("Bearer ")) {
                System.out.println("DEBUG: No Bearer token found in header."); // DEBUG
				return null;
			}
            String token = header.replace("Bearer ", "").trim();
            System.out.println("DEBUG: Token extracted: " + token.substring(0, 30) + "..."); // DEBUG: Cuidado para não logar token completo em prod!
			return token;		
		}
		
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws IOException, ServletException {
            System.out.println("DEBUG: doFilterInternal called for path: " + request.getRequestURI()); // DEBUG
			String token = getTokenFromHeader(request);
			
			if(token == null) {
                System.out.println("DEBUG: Token is null, continuing filter chain."); // DEBUG
				filterChain.doFilter(request, response);
				return;
			}
			
			DecodedJWT decodedJWT = jwtProvider.validateToken(token);
			if(decodedJWT == null) {
                System.out.println("DEBUG: Decoded JWT is NULL. Token validation failed."); // DEBUG
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Inválido");
				return;
			}
			
			String role = decodedJWT.getClaim("roles").asString();
            System.out.println("DEBUG: Token decoded. Subject (User ID): " + decodedJWT.getSubject() + ", Role from token: " + role); // DEBUG
            System.out.println("DEBUG: Assigning authority: ROLE_" + role); // DEBUG
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					decodedJWT.getSubject(),
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
				);
			
			SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("DEBUG: Authentication set in SecurityContextHolder."); // DEBUG
			filterChain.doFilter(request, response);
			
		}	
	
}