package it.xpug.kata.birthday_greetings.ports;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.sun.mail.util.MailConnectException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JakartaSmtpMailClientIntegrationTest {

	private static final int NONSTANDARD_PORT = 9998;
	private MailClient mailClient;
	private GreenMail mailServer;

	@BeforeEach
	public void setUp() {
		mailServer = new GreenMail(new ServerSetup(NONSTANDARD_PORT, null, ServerSetup.PROTOCOL_SMTP));
		mailServer.start();
		mailClient = new JakartaSmtpMailClient("mailclient.sender@email.org", "localhost", NONSTANDARD_PORT);
	}

	@AfterEach
	public void tearDown() {
		mailServer.stop();
	}

	@Test
	public void sendMessage() throws Exception {
		mailClient.sendMessage("The email subject", "This is the email body", "recipient@email.org");

		assertThat(mailServer.getReceivedMessages().length)
			.as("message not sent?")
			.isEqualTo(1);

		MimeMessage message = mailServer.getReceivedMessages()[0];
		assertThat(message.getSubject())
			.isEqualTo("The email subject");
		assertThat(GreenMailUtil.getBody(message))
			.isEqualTo("This is the email body");
		assertThat(message.getFrom())
			.hasSize(1);
		assertThat(message.getFrom()[0].toString())
			.isEqualTo("mailclient.sender@email.org");
		assertThat(message.getAllRecipients())
			.hasSize(1);
		assertThat(message.getAllRecipients()[0].toString())
			.isEqualTo("recipient@email.org");
	}

	@Test
	public void attemptToSendWithWrongPortConfig() {
		JakartaSmtpMailClient mailClientWithWrongConfig = new JakartaSmtpMailClient("any@email.com", "localhost", 1234);
		assertThatThrownBy(() ->
			mailClientWithWrongConfig.sendMessage("any", "any", "any@mail.com")
		)
			.isExactlyInstanceOf(MailConnectException.class)
			.hasMessage("Couldn't connect to host, port: localhost, 1234; timeout -1");
	}

	@Test
	public void attemptToSendWithWrongHostConfig() {
		JakartaSmtpMailClient mailClientWithWrongConfig = new JakartaSmtpMailClient("any@email.com", "wronghost", NONSTANDARD_PORT);
		assertThatThrownBy(() ->
			mailClientWithWrongConfig.sendMessage("any", "any", "any@mail.com")
		)
			.isExactlyInstanceOf(MailConnectException.class)
			.hasMessage("Couldn't connect to host, port: wronghost, 9998; timeout -1");
	}

}
