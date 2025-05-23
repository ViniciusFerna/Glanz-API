package com.eventos.glanz.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nameEvent;
	
	private Date date;
	
	private String description;
	
	private boolean visible;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Guests> guests = new ArrayList<>();
	
	public void addGuest(Guests guests) {
		this.guests.add(guests);
		guests.setEvent(this);
	}
	
	public void removeGuest(Guests guests) {
		this.guests.remove(guests);
		guests.setEvent(null);
	}
	
	public void confirmGuest(Long guestId) {
		for (Guests guest : guests) {
	        if (guest.getId().equals(guestId)) {
	            guest.setConfirmed(true);
	            return;
	        }
	    }
	    throw new RuntimeException("Convidado não encontrado no evento");
	}
	
	
	
	
	
	
	
	
}
