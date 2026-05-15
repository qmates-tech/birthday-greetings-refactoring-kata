package it.xpug.kata.birthday_greetings;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.GreenMailUtil;
import jakarta.mail.internet.MimeMessage;

public class AcceptanceTest {

	private static final int NONSTANDARD_PORT = 9999;
	private BirthdayService birthdayService;
	private GreenMail mailServer;

	@BeforeEach
	public void setUp() throws Exception {
		mailServer = new GreenMail(new ServerSetup(NONSTANDARD_PORT, null, ServerSetup.PROTOCOL_SMTP));
		mailServer.start();
		birthdayService = new BirthdayService();
	}

	@AfterEach
	public void tearDown() throws Exception {
		mailServer.stop();
	}

	@Test
	public void willSendGreetings_whenItsSomebodysBirthday() throws Exception {
		birthdayService.sendGreetings("employee_data.txt", new XDate("2008/10/08"), "localhost", NONSTANDARD_PORT);

		assertThat(mailServer.getReceivedMessages().length)
			.as("message not sent?")
			.isEqualTo(1);

		MimeMessage message = mailServer.getReceivedMessages()[0];
		assertThat(GreenMailUtil.getBody(message))
			.isEqualTo("Happy Birthday, dear John!");
		assertThat(message.getSubject())
			.isEqualTo("Happy Birthday!");
		assertThat(message.getFrom())
			.hasSize(1);
		assertThat(message.getFrom()[0].toString())
			.isEqualTo("sender@here.com");
		assertThat(message.getAllRecipients())
			.hasSize(1);
		assertThat(message.getAllRecipients()[0].toString())
			.isEqualTo("john.doe@foobar.com");
	}

	@Test
	public void willNotSendEmailsWhenNobodysBirthday() throws Exception {
		birthdayService.sendGreetings("employee_data.txt", new XDate("2008/01/01"), "localhost", NONSTANDARD_PORT);

		assertThat(mailServer.getReceivedMessages().length)
			.as("what? messages?")
			.isZero();
	}

	@Test
	public void willSendGreetingsToMultipleEmployees_whenItsSomebodysBirthday() throws Exception {
		birthdayService.sendGreetings("employee_data.txt", new XDate("2026/12/05"), "localhost", NONSTANDARD_PORT);

		MimeMessage[] sentMessages = mailServer.getReceivedMessages();
		assertThat(sentMessages.length).as("message not sent?").isEqualTo(2);

		assertThat(sentMessages).anySatisfy(message -> {
			assertThat(GreenMailUtil.getBody(message)).isEqualTo("Happy Birthday, dear Robert!");
			assertThat(message.getSubject()).isEqualTo("Happy Birthday!");
			assertThat(message.getFrom()).hasSize(1);
			assertThat(message.getFrom()[0].toString()).isEqualTo("sender@here.com");
			assertThat(message.getAllRecipients()).hasSize(1);
			assertThat(message.getAllRecipients()[0].toString()).isEqualTo("unclebob@cleancode.com");
		});
		assertThat(sentMessages).anySatisfy(message -> {
			assertThat(GreenMailUtil.getBody(message)).isEqualTo("Happy Birthday, dear Kent!");
			assertThat(message.getSubject()).isEqualTo("Happy Birthday!");
			assertThat(message.getAllRecipients()).hasSize(1);
			assertThat(message.getAllRecipients()[0].toString()).isEqualTo("kent@xp.com");
		});
	}

	@Test
	public void twentyNineOfFebruarySpecialTestCase() throws Exception {
		birthdayService.sendGreetings("employee_data.txt", new XDate("2026/02/28"), "localhost", NONSTANDARD_PORT);
		assertThat(mailServer.getReceivedMessages().length).as("what? messages?").isEqualTo(0);
		birthdayService.sendGreetings("employee_data.txt", new XDate("2026/03/01"), "localhost", NONSTANDARD_PORT);
		assertThat(mailServer.getReceivedMessages().length).as("what? messages?").isEqualTo(0);

		birthdayService.sendGreetings("employee_data.txt", new XDate("2028/02/29"), "localhost", NONSTANDARD_PORT);
		assertThat(mailServer.getReceivedMessages().length).as("message not sent?").isEqualTo(1);
		assertThat(mailServer.getReceivedMessages()[0].getAllRecipients()[0].toString()).isEqualTo("leap@year.org");
	}

}
