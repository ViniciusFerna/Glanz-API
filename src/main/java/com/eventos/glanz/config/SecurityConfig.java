package com.eventos.glanz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private SecurityUserFilter securityUserFilter;
	
	private static final String[] PERMIT_URLS = {
			"user/login",
			"user/",
			"user/criarUser",
			"user/deletarUser/"
	};
	
	@Bean
	// Filtro de segurança 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf - Cross-Site Request Forgery
		// csrf.disable() -> Que ele não vai usar sessão, é token
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorizeRequests -> {
			// rotas liberadas
			authorizeRequests
				.requestMatchers(PERMIT_URLS).permitAll();
			
			// outras rotas, deve estar autenticado
			authorizeRequests.anyRequest().authenticated();
		}).addFilterBefore(securityUserFilter, BasicAuthenticationFilter.class);
		// Retorno a requisição quando a sessão é desabilitada                                                                      
		return http.build();
	}
	
}
