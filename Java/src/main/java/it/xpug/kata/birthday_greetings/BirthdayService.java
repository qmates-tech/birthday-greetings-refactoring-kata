package it.xpug.kata.birthday_greetings;

import it.xpug.kata.birthday_greetings.ports.EmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.MailClient;
import it.xpug.kata.birthday_greetings.ports.XCalendar;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class BirthdayService {

	private final EmployeeRepository employeeRepository;
	private final MailClient mailClient;
	private final XCalendar calendar;

	public BirthdayService(EmployeeRepository employeeRepository, MailClient mailClient, XCalendar calendar) {
		this.employeeRepository = employeeRepository;
		this.mailClient = mailClient;
		this.calendar = calendar;
	}

	public void sendGreetings() throws IOException, MessagingException {
		XDate todayDate = this.calendar.today();
		List<Employee> employees = this.employeeRepository.getAll();
		for (Employee employee : employees) {
			if (!employee.isBirthday(todayDate))
				continue;

			this.mailClient.sendMessage(
				"Happy Birthday!",
				"Happy Birthday, dear %NAME%!".replace("%NAME%", employee.getFirstName()),
				employee.getEmail()
			);
		}
	}

}
