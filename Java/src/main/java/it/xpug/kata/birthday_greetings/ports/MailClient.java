package it.xpug.kata.birthday_greetings.ports;

import jakarta.mail.MessagingException;

public interface MailClient {
	void sendMessage(String subject, String body, String recipient) throws MessagingException;
}
