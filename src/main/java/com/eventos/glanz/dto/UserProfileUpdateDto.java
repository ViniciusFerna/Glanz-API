package com.eventos.glanz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileUpdateDto {

	@NotBlank(message = "O nome não pode estar vazio")
    private String name;
	
	private String phone;
	
}
