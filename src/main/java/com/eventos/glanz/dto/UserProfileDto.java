package com.eventos.glanz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {

	@NotBlank
    private String name;
    
    @NotBlank
    @Email
    private String email;
    
    private String phone;
    
    private String gender;
	
}
