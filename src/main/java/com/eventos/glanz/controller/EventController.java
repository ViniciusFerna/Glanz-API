package com.eventos.glanz.controller;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/eventos")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Retorna todos os eventos
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Evento>> getAllEvents() {
        List<Evento> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<Evento>> getVisibleEvents() {
			List<Evento> visibleEvents = eventService.getVisibleEvents();
			return new ResponseEntity<>(visibleEvents, HttpStatus.OK);
    }

    // Retorna um evento específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Cria um novo evento
    @PostMapping("/criarEvento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> createEvent(@RequestBody Evento event) {
        Evento createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // Atualiza um evento existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> updateEvent(@PathVariable Long id, @RequestBody Evento eventDetails) {
        try {
            Evento updatedEvent = eventService.updateEvent(id, eventDetails);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Remove um evento
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}