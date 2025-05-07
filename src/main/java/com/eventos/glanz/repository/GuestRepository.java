package com.eventos.glanz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventos.glanz.model.Guests;

public interface GuestRepository extends JpaRepository<Guests, Long>{

}
