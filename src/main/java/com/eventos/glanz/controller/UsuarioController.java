package com.eventos.glanz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/")
	public ResponseEntity<?> createUser() {
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}

	
	
}
