package it.xpug.kata.birthday_greetings;

import it.xpug.kata.birthday_greetings.ports.EmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.MailClient;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class BirthdayService {

	private final EmployeeRepository employeeRepository;
	private final MailClient mailClient;

	public BirthdayService(EmployeeRepository employeeRepository, MailClient mailClient) {
		this.employeeRepository = employeeRepository;
		this.mailClient = mailClient;
	}

	public void sendGreetings(XDate xDate, String smtpHost, int smtpPort) throws IOException, MessagingException {
		List<Employee> employees = this.employeeRepository.getAll();
		for (Employee employee : employees) {
			if (employee.isBirthday(xDate)) {
				String recipient = employee.getEmail();
				String body = "Happy Birthday, dear %NAME%!".replace("%NAME%", employee.getFirstName());
				String subject = "Happy Birthday!";
				mailClient.sendMessage(subject, body, recipient);
			}
		}
	}

}
