package com.eventos.glanz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.dto.GuestRequestDTO;
import com.eventos.glanz.exceptions.ResourceNotFoundException;
import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.Guests;
import com.eventos.glanz.repository.EventoRepository;
import com.eventos.glanz.repository.GuestRepository;
import com.eventos.glanz.service.GuestConfirmationEventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/convidado")
public class GuestController {
	
	@Autowired
	private GuestRepository guestRep;
	
	@Autowired
	private EventoRepository eventRep;
	
	@Autowired
	private GuestConfirmationEventService emailService;
	
	
	// Essa rota vai fazer com que o convidado que o dono do evento adicionar seja colocado na lista do evento
	// e vai enviar o email para ele, os dados que essa rota precisa para funcionar são: o nome do convidado, o email do convidado e o Id do evento que ele está sendo convidado
	// Acho que seria bom implementar essa rota na tela de eventos do admin, já que eu não acho que seria legal
	// deixar na mão do cliente essa parte de adicionar convidados, ainda mais que a quantidade de convidados
	// será decidida com a equipe pelo Whatsapp com antecedência
	@PostMapping("/addConvidado")
	public ResponseEntity<?> addGuest(@RequestBody @Valid GuestRequestDTO guestRequest) {
		try {
			
			if (guestRequest.getName() == null || guestRequest.getName().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O convidado deve ter um nome");
			}
			if (guestRequest.getEmail() == null || guestRequest.getEmail().isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O convidado deve ter um email");
			}
			if (guestRequest.getEventId() == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("O evento é obrigatório");
			}
			
			// vendo se o evento passado existe
			Evento evento = eventRep.findById(guestRequest.getEventId())
					.orElseThrow(() -> new RuntimeException("Evento não encontrado"));
			
			Guests newGuest = new Guests();
			newGuest.setName(guestRequest.getName());
			newGuest.setEmail(guestRequest.getEmail());
			// a presença do convidado ainda não está confirmada então eu passo o setConfirmed como false
			newGuest.setConfirmed(false);
			
			// marcando esse convidado para esse evento
			newGuest.setEvent(evento);
			
			// salvar o convidado para gerar o id
			guestRep.save(newGuest);
			
			// método do model evento
			evento.addGuest(newGuest);
			
			eventRep.save(evento);
			
			emailService.sendConfirmationEmail(newGuest, evento);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(newGuest);
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
		}
	}
	
	
	// Essa rota é a rota que a pessoa vai receber por email para aceitar o convite
	// A tela dessa rota não precisa ser muito elaborada, acho que só um "Convite aceito com sucesso" (que é o body que já é passado no return)
	// e talvez um botão de voltar para a home ou para o perfil
	@GetMapping("/aceitarConvite")
	public ResponseEntity<?> acceptInvite(@RequestParam Long eventId, @RequestParam Long guestId) {
		try {
			Evento event = eventRep.findById(eventId)
	                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
	        
	        Guests guest = guestRep.findById(guestId)
	                .orElseThrow(() -> new ResourceNotFoundException("Convidado não encontrado"));
			
			guest.setConfirmed(true);
			guestRep.save(guest);
			
			return ResponseEntity.ok().body("Convite aceito com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
		}
	}
	
	
	
}
