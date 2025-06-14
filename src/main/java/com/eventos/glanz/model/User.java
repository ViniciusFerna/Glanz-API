package com.eventos.glanz.model;

import com.eventos.glanz.util.HashUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "Usuario")
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private String role;
	
	@JsonProperty(access = Access.READ_WRITE)
	private String password;
	
	private String gender;
	
	@OneToOne
	@JoinColumn(name = "event_id", referencedColumnName = "id", nullable = true)
	private Evento eventOwner;
	
	public void setPassword(String password) {
		this.password = HashUtil.hash(password);
	}
	
}
