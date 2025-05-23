package com.eventos.glanz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.repository.EventoRepository;

@RestController
@RequestMapping("/eventos")
public class EventController {
	
	@Autowired
	private EventoRepository eventRep;
	
	@GetMapping("/")
	public ResponseEntity<?> getEvent() {
		try {
			List<Evento> evento = eventRep.findAll();
			
			return ResponseEntity.ok(evento);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	@PostMapping("/criarEvento")
	public ResponseEntity<?> createEvent(@RequestBody Evento evento) {
		try {
			
			Evento novoEvento = eventRep.save(evento);
			return ResponseEntity.ok(novoEvento);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
		}
	}
	
	
	
}
