package com.eventos.glanz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private SecurityUserFilter securityUserFilter;
	
	@Bean
	// Filtro de segurança 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf - Cross-Site Request Forgery
		// csrf.disable() -> Que ele não vai usar sessão, é token
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/user/login", "/user/registrar", "/convidado/addConvidado", "/convidado/aceitarConvite", "/swagger-ui/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/{id}", "/eventos/{id}", "/eventos/").authenticated()
				.requestMatchers(HttpMethod.PUT, "/user/{id}").authenticated()
				.requestMatchers("/user/deletarUser").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/admin/**", "/eventos/criarEvento", "/eventos/all").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(securityUserFilter, BasicAuthenticationFilter.class);
		// Retorno a requisição quando a sessão é desabilitada                                                                      
		return http.build();
	}
	
}
