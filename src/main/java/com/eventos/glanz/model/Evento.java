package com.eventos.glanz.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String title;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotBlank(message = "Localização é obrigatória")
    @Size(max = 200, message = "Localização deve ter no máximo 200 caracteres")
    private String location;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private String status; // Pode ser: "PLANEJADO", "EM ANDAMENTO", "CONCLUIDO", "CANCELADO"
    
    private boolean visible = true;
    
    private boolean hasOwner = false;
	
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
