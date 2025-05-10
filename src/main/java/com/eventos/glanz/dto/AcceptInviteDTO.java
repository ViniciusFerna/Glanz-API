package com.eventos.glanz.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AcceptInviteDTO {
	
	@NotNull(message = "O ID do evento é obrigatório")
	private Long eventId;
	
	@NotNull(message = "O ID do convidado é obrigatório")
	private Long guestId;
	
}
