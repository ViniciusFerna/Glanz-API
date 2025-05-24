package com.eventos.glanz.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eventos.glanz.model.Evento;

public class EventValidator {

	public static List<String> validate(Evento evento, boolean isUpdate) {
        List<String> errors = new ArrayList();
        
        if (evento.getNameEvent() == null || evento.getNameEvent().trim().isEmpty()) {
            errors.add("O nome do evento não pode ser nulo ou vazio");
        }
        
        if (evento.getDate() == null) {
            errors.add("A data do evento não pode ser nula");
        } else if (evento.getDate().before(new Date())) {
            errors.add("A data do evento deve ser futura");
        }
        
        if (!isUpdate || evento.getDescription() != null) {
            if (evento.getDescription() == null || evento.getDescription().trim().isEmpty()) {
                errors.add("A descrição do evento não pode ser nula ou vazia");
            }
        }
        
        return errors;
    }
	
}
