package it.xpug.kata.birthday_greetings.ports;

public class FakeMailClient implements MailClient {

	@Override
	public void sendMessage(String subject, String body, String recipient) {
		// do nothing !
	}
}
