package com.eventos.glanz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventos.glanz.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
