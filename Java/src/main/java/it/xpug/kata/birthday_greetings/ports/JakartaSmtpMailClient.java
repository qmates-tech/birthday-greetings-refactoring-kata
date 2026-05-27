package it.xpug.kata.birthday_greetings.ports;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

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
		Session session = createMailSession();
		Message msg = buildMessage(subject, body, recipient, session);
		Transport.send(msg);
	}

	private Session createMailSession() {
		Properties sessionProperties = new Properties() {{
			put("mail.smtp.host", smtpHost);
			put("mail.smtp.port", "" + smtpPort);
		}};
		return Session.getInstance(sessionProperties, null);
	}

	private Message buildMessage(String subject, String body, String recipient, Session session) throws MessagingException {
		return new MimeMessage(session) {{
			setFrom(new InternetAddress(senderAddress));
			setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			setSubject(subject);
			setText(body);
		}};
	}
}
