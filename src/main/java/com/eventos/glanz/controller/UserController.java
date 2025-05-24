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
import com.eventos.glanz.dto.UserUpdateDto;
import com.eventos.glanz.dto.loginDTO;
import com.eventos.glanz.exceptions.ResourceNotFoundException;
import com.eventos.glanz.model.User;
import com.eventos.glanz.repository.UserRepository;
import com.eventos.glanz.util.HashUtil;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	// Login é o básico, na lógica aqui do back só tem email e senha
	// mas temos que descobrir uma maneira de armazenar o token que é passado no return
	// e alguma maneira de fazer o JavaScript diferenciar os dados do token que estão criptografados em Sha256.
	// A chave secreta e o salt estão no authenticateUserCase, HashUtil e no application.properties
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
	
	
	// Esse é o registrar para o usuário criar sua conta, e os dados que ele precisa receber são:
	// Nome, email, senha(se possível ter o sistema de colocar duas vezes a mesma senha), telefone e gênero
	@PostMapping("/registrar")
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
	
	// Essa é a rota para o cliente visualizar o próprio usuário, e os dados que ele mostra são:
	// Nome, email, telefone e o genêro
	// Nessa tela também vão ter os botões de atualizar usuario e deletar usuario que vão estar aqui em baixo
	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getUser(@PathVariable Long id, Authentication authentication) {
		// O Authentication e um objeto que vai conter os dados do usuario logado no momento por meio do token passado no header
		try {
			// Aqui o authentication.getName() esta pegando o id do user logado e o comparando com o id passado na requisicao
			Long userId = Long.parseLong(authentication.getName());
	        if (!id.equals(userId)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                   .body("Você só pode visualizar seus próprios dados");
	        }

	        // Busca o usuário
	        User user = userRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

	        // Retorna DTO (não retorna a entidade diretamente)
	        UserProfileDto response = new UserProfileDto(
	            user.getName(),
	            user.getEmail(),
	            user.getPhone(),
	            user.getGender()
	        );
	        
	        return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
		
	}
	
	// Esse botão pode ser simples
	// Só uma coisa que eu queria seria que quando a pessoa aperta o botão que abra uma tela em cima do perfil
	// e que nessa tela apareça se a pessoa deseja mesmo excluir o usuário dela, e nessa tela sim o botão vai estar 
	// conectado nessa rota para deletar o usuário e caso a pessoa delete o usuário ela deve voltar para o home sem estar logada
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
	
	
	// Essa rota só precisa de um botão no perfil de "atualizar usuário" e o jeito que fizer ta bom,
	// pode abrir uma outra pagina ou fazer na mesma só precisa funcionar
	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateDto userUpdateDto, Authentication authentication) {
		try {
			
			Long userId = Long.parseLong(authentication.getName());
			if (!id.equals(userId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
		                   .body("Você só pode atualizar os seus próprios dados");
			}
			
			User user = userRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
			
			
					user.setName(userUpdateDto.getName());
				 	user.setEmail(userUpdateDto.getEmail());
				 	user.setPassword(userUpdateDto.getPassword());
				 	user.setPhone(userUpdateDto.getPhone());
				 	user.setGender(userUpdateDto.getGender());
			
				 	// Criptografa a senha nova passada caso ela seja passada
			if (userUpdateDto.getPassword() != null) {
				user.setPassword(HashUtil.hash(userUpdateDto.getPassword()));
			}
			
			userRepo.save(user);
			return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso");
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
}
