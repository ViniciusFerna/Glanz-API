package com.eventos.glanz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evento.glanz.dto.AcceptInviteDTO;
import com.evento.glanz.dto.GuestRequestDTO;
import com.eventos.glanz.exceptions.ResourceNotFoundException;
import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.Guests;
import com.eventos.glanz.repository.EventoRepository;
import com.eventos.glanz.repository.GuestRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/convidado")
public class GuestController {
	
	@Autowired
	private GuestRepository guestRep;
	
	@Autowired
	private EventoRepository eventRep;
	
	@PostMapping("/addGuest")
	public ResponseEntity<?> addGuest(@RequestBody GuestRequestDTO guestRequest) {
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
			// método do model evento
			evento.addGuest(newGuest);
			
			eventRep.save(evento);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(newGuest);
			
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
		}
	}
	
	@PostMapping("/aceitarConvite")
	public ResponseEntity<?> acceptInvite(@Valid @RequestBody AcceptInviteDTO acceptInviteDto) {
		try {
			Evento evento = eventRep.findById(acceptInviteDto.getEventId())
					.orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
			
			evento.confirmGuest(acceptInviteDto.getGuestId());
			eventRep.save(evento);
			
			return ResponseEntity.ok().body("Convite aceito com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor: " + e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
}
