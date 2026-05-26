package it.xpug.kata.birthday_greetings;

import java.io.*;
import java.text.ParseException;

import it.xpug.kata.birthday_greetings.ports.FileEmployeeRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class Main {

	public static void main(String[] args) throws AddressException, IOException, ParseException, MessagingException {
		BirthdayService service = new BirthdayService(
			new FileEmployeeRepository("employee_data.txt")
		);
		service.sendGreetings(new XDate(), "localhost", 25);
	}

}
