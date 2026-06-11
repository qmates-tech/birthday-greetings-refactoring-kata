package it.xpug.kata.birthday_greetings;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import it.xpug.kata.birthday_greetings.ports.FakeMailClient;
import it.xpug.kata.birthday_greetings.ports.FakeXCalendar;
import it.xpug.kata.birthday_greetings.ports.FileEmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.JakartaSmtpMailClient;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("Convert2MethodRef")
public class AcceptanceTest {

	private static final int NONSTANDARD_PORT = 9999;
	private BirthdayService birthdayService;
	private GreenMail mailServer;
	private FakeXCalendar fakeXCalendar;

	@BeforeEach
	public void setUp() {
		mailServer = new GreenMail(new ServerSetup(NONSTANDARD_PORT, null, ServerSetup.PROTOCOL_SMTP));
		mailServer.start();
		fakeXCalendar = new FakeXCalendar();
		birthdayService = new BirthdayService(
			new FileEmployeeRepository("employee_data.txt"),
			new JakartaSmtpMailClient("sender@here.com", "localhost", NONSTANDARD_PORT),
			fakeXCalendar
		);
	}

	@AfterEach
	public void tearDown() {
		mailServer.stop();
	}

	@Test
	public void willSendGreetings_whenItsSomebodysBirthday() throws Exception {
		fakeXCalendar.setTodayDate(new XDate("2008/10/08"));

		birthdayService.sendGreetings();

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
		fakeXCalendar.setTodayDate(new XDate("2008/01/01"));

		birthdayService.sendGreetings();

		assertThat(mailServer.getReceivedMessages().length)
			.as("what? messages?")
			.isZero();
	}

	@Test
	public void willSendGreetingsToMultipleEmployees_whenItsSomebodysBirthday() throws Exception {
		fakeXCalendar.setTodayDate(new XDate("2026/12/05"));

		birthdayService.sendGreetings();

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
	public void willThrowAnException_whenLoadedWithAnUnexistingEmployeeFile() {
		BirthdayService birthdayServiceWithUnexistingEmployeeFile = new BirthdayService(
			new FileEmployeeRepository("unexisting_file.txt"),
			new FakeMailClient(),
			new FakeXCalendar()
		);
		assertThatThrownBy(() ->
			birthdayServiceWithUnexistingEmployeeFile.sendGreetings()
		)
			.isExactlyInstanceOf(NoSuchFileException.class)
			.hasMessage("unexisting_file.txt");
	}

	@Test
	public void willThrowAnExceptionWithoutSendingEmails_whenLoadedWithAPartiallyBrokenEmployeeData() {
		BirthdayService birthdayServiceWithPartiallyBrokenEmployeeFile = new BirthdayService(
			new FileEmployeeRepository("employee_partially_broken_data.txt"),
			new FakeMailClient(),
			new FakeXCalendar()
		);
		assertThatThrownBy(() ->
			birthdayServiceWithPartiallyBrokenEmployeeFile.sendGreetings()
		)
			.isExactlyInstanceOf(ArrayIndexOutOfBoundsException.class)
			.hasMessage("Index 3 out of bounds for length 3");

		// BEHAVIOR CHANGED HERE !
		assertThat(mailServer.getReceivedMessages().length).as("what? messages?").isEqualTo(0);
	}

}
