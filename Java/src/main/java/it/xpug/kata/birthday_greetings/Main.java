package it.xpug.kata.birthday_greetings;

import java.io.*;

import it.xpug.kata.birthday_greetings.ports.FileEmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.JakartaSmtpMailClient;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class Main {

	public static void main(String[] args) throws AddressException, IOException, MessagingException {
		BirthdayService service = new BirthdayService(
			new FileEmployeeRepository("employee_data.txt"),
			new JakartaSmtpMailClient("sender@here.com", "localhost", 25)
		);
		service.sendGreetings(new XDate(), "localhost", 25);
	}

}
