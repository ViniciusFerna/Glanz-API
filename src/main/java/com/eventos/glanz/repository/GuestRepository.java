package com.eventos.glanz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.Guests;

public interface GuestRepository extends JpaRepository<Guests, Long>{
	
	List<Guests> findByEvent(Evento event);

}
