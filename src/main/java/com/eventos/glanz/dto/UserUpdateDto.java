package com.eventos.glanz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
	
	@NotBlank(message = "O nome não pode estar vazio")
    private String name;
    
	@NotBlank(message = "O email não pode estar vazio")
    @Email
    private String email;
    
    private String phone;
    
    private String gender;
    
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

}
