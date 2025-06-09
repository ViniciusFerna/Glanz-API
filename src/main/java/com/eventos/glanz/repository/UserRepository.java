package com.eventos.glanz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByEmail(String email);

	public User findByEmail(String email);
	
	User findByEventOwner(Evento eventOwner); 
	
	Optional<User> findByEventOwnerId(Long eventId);
	
}
