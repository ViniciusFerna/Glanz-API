package com.eventos.glanz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmail(String email);
	
	User findByEventOwner(Evento eventOwner); 
	
}
