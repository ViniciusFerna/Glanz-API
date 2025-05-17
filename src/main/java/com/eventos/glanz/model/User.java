package com.eventos.glanz.model;

import com.eventos.glanz.util.HashUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private boolean adminPerms;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String password;
	
	private String gender;
	
	@OneToOne
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private Evento eventOwner;
	
	@ManyToOne
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	
	public void setPassword(String password) {
		this.password = HashUtil.hash(password);
	}
	
}
