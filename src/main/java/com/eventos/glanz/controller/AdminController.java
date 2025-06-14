package com.eventos.glanz.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.User;
import com.eventos.glanz.repository.EventoRepository;
import com.eventos.glanz.repository.UserRepository;
import com.eventos.glanz.util.HashUtil;

@RestController
@CrossOrigin

// Descobrir maneira de pegar os dados do token para que apenas admins possam acessar essa rota
// No token são passados os seguintes dados: ID, email e roles
// E lembrando que o ID só é passado quando a pessoa faz login
// Então descobrir maneira de armazenar o token no browser e usar os dados contidos nele
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	private EventoRepository eventRepo;
	
	@Autowired
	private UserRepository userRepo;
	// Aqui é a tela do admin, onde ele tem acesso a todas as contas, podendo deletar, atualizar e visualizar qualquer conta de qualquer usuário
	// Já que essa rota é a que lista todos os usuário que tem cadatro no site e todos os dados deles eu acho que seja melhor de ser a tela inicial da parte de admin
	// visto que da pra fazer uma div grande e dentro dela colocar os usuários dentro de cards separados para cada um, dentro desses cards tem que ter os seguintes dados:
	// Nome, email, telefone, as roles do user, o genêro, e o Id de um evento que essa pessoa possa ter(caso ela não tenha nenhum evento cadastrado pode aparcer "sem eventos criados" ou algo do tipo
	@GetMapping("/")
	public ResponseEntity<?> getUsers() {
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
	
	// Esse é o botão de deletar, só que nesse caso ele pode deletar qualquer user
	// Eu acho melhor colocar em cada card do de cima um botão de deletar e um de atualizar
	// com cada botão desses pegando o id do usuário do card sem ter que o admin informar nada para deletar
	// Lembrando que o id para ser deletado é passado na rota depois do /
	@DeleteMapping("/deleteUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserAdmin(@PathVariable("id") Long id) {
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
	
	@PutMapping("/updateUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateUserAdmin(@PathVariable("id") Long id, @RequestBody User user) {
		try {
			
			Optional<User> verificarSeExisteUser = userRepo.findById(id);			
			
			if(verificarSeExisteUser.isPresent()) {
				
				User UserN = verificarSeExisteUser.get();
				 	UserN.setName(user.getName());
				 	UserN.setEmail(user.getEmail());
				 	UserN.setPassword(user.getPassword());
				 	UserN.setPhone(user.getPhone());
				 	UserN.setGender(user.getGender());
				 	UserN.setRole(user.getRole());
				 	UserN.setEventOwner(user.getEventOwner());
				 	
				 // Criptografa a senha nova passada caso ela seja passada
					if (user.getPassword() != null) {
						user.setPassword(HashUtil.hash(user.getPassword()));
					}
				
				 	userRepo.save(UserN);
				 	
				 	return ResponseEntity.ok(UserN);
				 	
			}
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O usuário passado não existe");
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	@PutMapping("/placeEvent/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> placeUserInEvent(@PathVariable("id") Long eventId, @RequestBody User user) {
		try {
			User userExistente = userRepo.findByEmail(user.getEmail());
			
			if (userExistente == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O usuário passado não existe");
			}
			
			Optional<Evento> eventoOpt = eventRepo.findById(eventId);
			
			if (eventoOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O evento passado não existe");
			}
			
			Evento evento = eventoOpt.get();
			
			if (userExistente.getEventOwner() != null && userExistente.getEventOwner().getId().equals(eventId)) {
                return ResponseEntity.status(HttpStatus.OK).body("Esse usuário já é o dono desse evento.");
            }
			
			userExistente.setEventOwner(evento);
			userRepo.save(userExistente);
			
			evento.setHasOwner(true);
            eventRepo.save(evento);
			
			return ResponseEntity.status(HttpStatus.OK).body("Esse usuário agora é dono desse evento");
			
		} catch (Exception e) {
		    // Log a exceção para ver o stack trace completo
		    e.printStackTrace(); // Apenas para depuração local, use um logger de verdade em produção
		    System.err.println("Erro ao vincular usuário ao evento: " + e.getMessage()); // Mensagem para console

		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
		}
	}
	
}
