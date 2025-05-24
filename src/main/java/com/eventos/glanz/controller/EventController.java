package com.eventos.glanz.controller;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import com.eventos.glanz.exceptions.EventValidator;
import com.eventos.glanz.model.Evento;
import com.eventos.glanz.repository.EventoRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/evento")
public class EventController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping("/")
    public ResponseEntity<?> listarEventos() {
        try {
            List<Evento> eventos = eventoRepository.findAll();

            if (eventos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum evento encontrado");
            }

            return ResponseEntity.ok(eventos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar eventos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEventoPorId(@PathVariable Long id) {
        try {
            return eventoRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao buscar evento: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> criarEvento(@RequestBody Evento evento) {
        List<String> errors = EventValidator.validate(evento, false);
        
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        
        try {
            Evento novoEvento = eventoRepository.save(evento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao criar evento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody Evento eventoAtualizado) {
        try {
            Evento eventoExistente = eventoRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Evento não encontrado com ID: " + id));

            List<String> errors = EventValidator.validate(eventoAtualizado, true);
            
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

            // Atualiza apenas os campos fornecidos
            if (eventoAtualizado.getNameEvent() != null) {
                eventoExistente.setNameEvent(eventoAtualizado.getNameEvent());
            }
            if (eventoAtualizado.getDate() != null) {
                eventoExistente.setDate(eventoAtualizado.getDate());
            }
            if (eventoAtualizado.getDescription() != null) {
                eventoExistente.setDescription(eventoAtualizado.getDescription());
            }

            Evento eventoSalvo = eventoRepository.save(eventoExistente);
            return ResponseEntity.ok(eventoSalvo);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Erro ao atualizar evento: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
        try {
            if (!eventoRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento não encontrado com ID: " + id);
            }

            eventoRepository.deleteById(id);
            return ResponseEntity.ok().body("Evento deletado com sucesso");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar evento: " + e.getMessage());
        }
    }
}
