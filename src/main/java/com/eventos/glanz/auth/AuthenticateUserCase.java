package com.eventos.glanz.auth;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eventos.glanz.model.User;
import com.eventos.glanz.repository.UserRepository;
import com.eventos.glanz.util.HashUtil;


public class AuthenticateUserCase {

	private final UserRepository repUser;
	private final String secretKey = "projetinhosenaifellas";
	private final long expiration = 86400000; // 1 dia em milissegundos 
	
	// construtor
	public AuthenticateUserCase(UserRepository repUser) {
		this.repUser = repUser;
	}
	
	public String execute(String email, String rawPassword) {
		User user = repUser.findByEmail(email);
		
		// Validando a senha fora do banco e comparando a senha escrita com a criptografada por motivos de segurança
		if (!HashUtil.verify(rawPassword, user.getPassword())) {
			throw new RuntimeException("Credenciais Incorretas");
		}
		
		return generateToken(user);
		
	}
	// gerar token
	public String generateToken(User user) {
		// Criando algoritmo com a secret
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		
		// Gerando token JWT
		return JWT.create()
				.withSubject(user.getId().toString())
				.withClaim("email", user.getEmail())
				.withClaim("roles", user.isAdminPerms())
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.sign(algorithm);
	}
	
}
