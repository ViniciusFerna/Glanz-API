package com.eventos.glanz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventos.glanz.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

	Optional<Evento> findById(Long id);
	
}
