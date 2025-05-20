package com.eventos.glanz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.auth.AuthenticateUserCase;
import com.eventos.glanz.dto.UserProfileDto;
import com.eventos.glanz.dto.loginDTO;
import com.eventos.glanz.exceptions.ResourceNotFoundException;
import com.eventos.glanz.model.User;
import com.eventos.glanz.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody loginDTO user) {
		try {
			AuthenticateUserCase auth = new AuthenticateUserCase(userRepo);
			
			String token = auth.execute(user.getEmail(), user.getPassword());
			
			return ResponseEntity.status(HttpStatus.OK).body(token);
			
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorreto");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	
	
	@PostMapping("/criarUser")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			if(user.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Digite o nome de usuario para criar um usuario");
			} else if(user.getPassword().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Digite uma senha para criar um usuario");
			}
			
			User newUser = userRepo.save(user);
			
			return ResponseEntity.ok(newUser);
				
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id, Authentication authentication) {
		try {
			Long userId = Long.parseLong(authentication.getName());
	        if (!id.equals(userId)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                   .body("Você só pode visualizar seus próprios dados");
	        }

	        // 2. Busca o usuário
	        User user = userRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

	        // 3. Retorna DTO seguro (não retorne a entidade diretamente!)
	        UserProfileDto response = new UserProfileDto(
	            user.getName(),
	            user.getEmail(),
	            user.getGender()
	        );
	        
	        return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
		
	}
	
	@DeleteMapping("/deletarUser")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteUser(Authentication authentication) {
		try {
			
			Long userId = Long.parseLong(authentication.getName());
			
			userRepo.deleteById(userId);
			
			return ResponseEntity.status(HttpStatus.OK).body("Seu usuário foi deletado com sucesso");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		try {
			Optional<User> verificarSeExisteUser = userRepo.findById(id);			
			
			if(verificarSeExisteUser.isPresent()) {
				
				User UserN = verificarSeExisteUser.get();
				 	UserN.setName(user.getName());
				 	UserN.setEmail(user.getEmail());
				 	UserN.setPassword(user.getPassword());
				 	UserN.setPhone(user.getPhone());
				 	UserN.setGender(user.getGender());
				
				 	userRepo.save(UserN);
				 	
				 	return ResponseEntity.ok(UserN);
				
			}
			
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O usuário passado não existe");
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
}
