package com.eventos.glanz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.model.User;
import com.eventos.glanz.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
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
	
	@GetMapping("/")
	public ResponseEntity<?> getUser() {
		try {
			List<User> users = userRepo.findAll();
			
			if(users.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Não há nenhum usuário");
			}
			
			return ResponseEntity.ok(users);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
		
	}
	
	@DeleteMapping("/deletarUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		try {
			Optional<User> verificarSeExisteUser = userRepo.findById(id);
			
			if(verificarSeExisteUser.isPresent()) {
				
				userRepo.deleteById(id);
				
				return ResponseEntity.ok("Usuário deletado com sucesso");
				
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O usuario passado não existe");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	@PutMapping("/{id}")
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
