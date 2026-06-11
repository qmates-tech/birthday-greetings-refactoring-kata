package it.xpug.kata.birthday_greetings;

import it.xpug.kata.birthday_greetings.ports.FileEmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.JakartaSmtpMailClient;
import it.xpug.kata.birthday_greetings.ports.RealXCalendar;
import jakarta.mail.MessagingException;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, MessagingException {
		BirthdayService service = new BirthdayService(
			new FileEmployeeRepository("employee_data.txt"),
			new JakartaSmtpMailClient("sender@here.com", "localhost", 25),
			new RealXCalendar()
		);
		service.sendGreetings();
	}

}
