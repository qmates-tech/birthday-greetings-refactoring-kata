package it.xpug.kata.birthday_greetings.ports;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class JakartaSmtpMailClient implements MailClient {

	private final String senderAddress;
	private final String smtpHost;
	private final int smtpPort;

	public JakartaSmtpMailClient(String senderAddress, String smtpHost, int smtpPort) {
		this.senderAddress = senderAddress;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
	}

	public void sendMessage(String subject, String body, String recipient) throws MessagingException {
		java.util.Properties props = new java.util.Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "" + smtpPort);
		Session session = Session.getInstance(props, null);

		// Construct the message
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(senderAddress));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		msg.setSubject(subject);
		msg.setText(body);

		// Send the message
		Transport.send(msg);
	}
}
