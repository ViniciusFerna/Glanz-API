package com.eventos.glanz.service;

import com.eventos.glanz.controller.EventController;
import com.eventos.glanz.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventos.glanz.repository.EventoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventoRepository eventRepository;

    @Autowired
    public EventService(EventoRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Evento> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Evento> getVisibleEvents() {
    	return eventRepository.findByVisibleTrue();
    }

    public Optional<Evento> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Evento createEvent(Evento event) {
        return eventRepository.save(event);
    }

    public Evento updateEvent(Long id, Evento eventDetails) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com id: " + id));

        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setEventDate(eventDetails.getEventDate());
        event.setStatus(eventDetails.getStatus());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com id: " + id));
        eventRepository.delete(event);
    }
}