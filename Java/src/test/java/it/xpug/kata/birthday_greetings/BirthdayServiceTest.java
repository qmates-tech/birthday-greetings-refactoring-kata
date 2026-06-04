package it.xpug.kata.birthday_greetings;

import it.xpug.kata.birthday_greetings.ports.EmployeeRepository;
import it.xpug.kata.birthday_greetings.ports.MailClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

class BirthdayServiceTest {

	private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
	private final MailClient mailClient = mock(MailClient.class);
	private final BirthdayService birthdayService = new BirthdayService(employeeRepository, mailClient);

	@Test
	void zeroEmployees_noGreetings() throws Exception {
		when(employeeRepository.getAll()).thenReturn(emptyList());

		birthdayService.sendGreetings(new XDate("2026/06/04"), "any", -1);

		verify(mailClient, never()).sendMessage(any(), any(), any());
	}

	@Test
	void someEmployees_noGreetings() throws Exception {
		when(employeeRepository.getAll()).thenReturn(List.of(
			new Employee("Kent", "Beck", "1961/06/05", "kent@xp.com"),
			new Employee("Robert", "Martin", "1952/12/05", "unclebob@cleancode.com")
		));

		birthdayService.sendGreetings(new XDate("2026/06/04"), "any", -1);

		verify(mailClient, never()).sendMessage(any(), any(), any());
	}

	@Test
	void sendGreetingsToAnEmployeesWhenItsBirthday() throws Exception {
		when(employeeRepository.getAll()).thenReturn(List.of(
			new Employee("Mary", "Ann", "1975/03/11", "mary.ann@foobar.com"),
			new Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com"),
			new Employee("Robert", "Martin", "1952/12/05", "unclebob@cleancode.com")
		));

		birthdayService.sendGreetings(new XDate("2026/03/11"), "any", -1);

		verify(mailClient, times(1)).sendMessage(any(), any(), any());
		verify(mailClient, times(1)).sendMessage(
			"Happy Birthday!",
			"Happy Birthday, dear Mary!",
			"mary.ann@foobar.com"
		);
	}

	@Test
	void sendGreetingsToMultipleEmployeesWhenTheirBirthday() throws Exception {
		when(employeeRepository.getAll()).thenReturn(List.of(
			new Employee("Robert", "Martin", "1952/12/05", "unclebob@cleancode.com"),
			new Employee("John", "Doe", "1982/10/08", "john.doe@foobar.com"),
			new Employee("Kent", "Beck", "1961/12/05", "kent@xp.com")
		));

		birthdayService.sendGreetings(new XDate("2026/12/05"), "any", -1);

		verify(mailClient, times(2)).sendMessage(any(), any(), any());
		verify(mailClient, times(1)).sendMessage(
			"Happy Birthday!",
			"Happy Birthday, dear Robert!",
			"unclebob@cleancode.com"
		);
		verify(mailClient, times(1)).sendMessage(
			"Happy Birthday!",
			"Happy Birthday, dear Kent!",
			"kent@xp.com"
		);
	}

}
