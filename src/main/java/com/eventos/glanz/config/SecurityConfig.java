package com.eventos.glanz.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


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
		http
		.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(List.of("*"));
			config.setAllowedMethods(List.of("*"));
			config.setAllowedHeaders(List.of("*"));
			return config;
		}))
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/user/login", "/user/registrar", "/swagger-ui/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/convidado/aceitarConvite").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/{id}", "/eventos/{id}", "/eventos/").authenticated()
				.requestMatchers(HttpMethod.PUT, "/user/{id}").authenticated()
				.requestMatchers("/user/deletarUser").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/admin/**", "/eventos/criarEvento", "/eventos/all", "/convidado/addConvidado").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(securityUserFilter, BasicAuthenticationFilter.class);
		// Retorno a requisição quando a sessão é desabilitada                                                                      
		return http.build();
	}
	
}
