package com.eventos.glanz.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class GuestRequestDTO {
	
	@NotNull(message = "O nome do convidado não pode ser vazio")
	private String name;
	
	@NotNull(message = "O email do convidado não pode ser vazio")
	private String email;
	
	private Long eventId;
	
}
