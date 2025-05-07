package com.eventos.glanz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventos.glanz.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
