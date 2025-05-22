package com.eventos.glanz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.Guests;

@Service
public class GuestConfirmationEventService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine template;
	
	public void sendConfirmationEmail(Guests guests, Evento event) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
			Context context = new Context();
			context.setVariable("guestName", guests.getName());
			context.setVariable("eventName", event.getNameEvent());
			context.setVariable("eventDate", event.getDate());
			context.setVariable("confirmationUrl", "http://localhost:8080/convidado/"
					.concat(String.valueOf(guests.getId())));
			
			String htmlContent = template.process("email/guest-confirmation", context);
			
			helper.setTo(guests.getEmail());
			helper.setTo("Confirmação de presença: " + event.getNameEvent());
			helper.setText(htmlContent, true);
		};
		mailSender.send(messagePreparator);
	}
	
}
