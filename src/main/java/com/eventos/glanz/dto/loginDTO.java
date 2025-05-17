package com.eventos.glanz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class loginDTO {

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email deve ser válido")
	private String email;
	
	@NotBlank(message = "Senha é obrigatório")
	private String password;
	
}
