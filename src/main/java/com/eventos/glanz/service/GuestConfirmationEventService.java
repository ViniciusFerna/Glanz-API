package com.eventos.glanz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.eventos.glanz.model.Evento;
import com.eventos.glanz.model.Guests;

import jakarta.mail.internet.MimeMessage;

@Service
public class GuestConfirmationEventService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine template;
	
	public void sendConfirmationEmail(Guests guests, Evento event) {
		try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // Prepara o contexto do template
            Context context = new Context();
            context.setVariable("guestName", guests.getName());
            context.setVariable("eventName", event.getNameEvent());
            context.setVariable("confirmationUrl", "http://localhost:8080/convidado/aceitarConvite?eventId="
					.concat(String.valueOf(event.getId())
					.concat("&guestId=")
					.concat(String.valueOf(guests.getId()))));
            
            // Processa o template Thymeleaf
            String htmlContent = template.process("email/guest-confirmation", context);
            
            // Configura o e-mail
            helper.setTo(guests.getEmail());
            helper.setSubject("Confirmação de Presença: " + event.getNameEvent());
            helper.setText(htmlContent, true); // true = conteúdo é HTML
            
            mailSender.send(message);
            
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar e-mail: " + e.getMessage(), e);
        }
	}
}
