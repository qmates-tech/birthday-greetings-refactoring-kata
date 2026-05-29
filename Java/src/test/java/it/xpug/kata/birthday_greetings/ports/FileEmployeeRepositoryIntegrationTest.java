package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.Employee;
import it.xpug.kata.birthday_greetings.XDate;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileEmployeeRepositoryIntegrationTest {

	@Test
	public void getAllFromFile() throws Exception {
		FileEmployeeRepository repository = new FileEmployeeRepository("employee_data.txt");

		List<Employee> employees = repository.getAll();

		assertThat(employees).hasSize(5);
		assertThat(employees).anySatisfy(employee -> {
			assertThat(employee.getFirstName()).isEqualTo("Robert");
			assertThat(employee.getEmail()).isEqualTo("unclebob@cleancode.com");
			assertThat(employee.isBirthday(new XDate(LocalDate.of(2026, Month.DECEMBER, 5)))).isTrue();
			assertThat(employee.toString()).isEqualTo("Employee Robert Martin <unclebob@cleancode.com> born XDate[date=1952-12-05]");
		});
		assertThat(employees).anySatisfy(employee -> {
			assertThat(employee.getFirstName()).isEqualTo("Kent");
			assertThat(employee.getEmail()).isEqualTo("kent@xp.com");
			assertThat(employee.toString()).isEqualTo("Employee Kent Beck <kent@xp.com> born XDate[date=1961-12-05]");
		});
		assertThat(employees).anySatisfy(employee ->
			assertThat(employee.toString()).isEqualTo("Employee John Doe <john.doe@foobar.com> born XDate[date=1982-10-08]")
		);
		assertThat(employees).anySatisfy(employee ->
			assertThat(employee.toString()).isEqualTo("Employee Mary Ann <mary.ann@foobar.com> born XDate[date=1975-03-11]")
		);
		assertThat(employees).anySatisfy(employee ->
			assertThat(employee.toString()).isEqualTo("Employee Year Leap <leap@year.org> born XDate[date=1992-02-29]")
		);
	}

	@Test
	public void throwsParseExceptionWithPartiallyBrokenData() {
		FileEmployeeRepository repository = new FileEmployeeRepository("employee_partially_broken_data.txt");

		//noinspection Convert2MethodRef
		assertThatThrownBy(() -> repository.getAll())
			.isExactlyInstanceOf(ArrayIndexOutOfBoundsException.class)
			.hasMessage("Index 3 out of bounds for length 3");
	}

	@Test
	public void throwsExceptionWithBrokenData() {
		FileEmployeeRepository repository = new FileEmployeeRepository("employee_broken_data1.txt");

		//noinspection Convert2MethodRef
		assertThatThrownBy(() -> repository.getAll())
			.isExactlyInstanceOf(ArrayIndexOutOfBoundsException.class)
			.hasMessage("Index 1 out of bounds for length 1");
	}

	@Test
	public void throwsParseExceptionWithBrokenBirthdayDateTime() {
		FileEmployeeRepository repository = new FileEmployeeRepository("employee_broken_data2.txt");

		//noinspection Convert2MethodRef
		assertThatThrownBy(() -> repository.getAll())
			.isExactlyInstanceOf(DateTimeParseException.class)
			.hasMessage("Text 'broken_date_time' could not be parsed at index 0");
	}

	@Test
	public void throwsFileNotFoundExceptionWithUnexistingFile() {
		FileEmployeeRepository repository = new FileEmployeeRepository("unexisting_file.txt");

		//noinspection Convert2MethodRef
		assertThatThrownBy(() -> repository.getAll())
			.isInstanceOf(IOException.class)
			.isExactlyInstanceOf(NoSuchFileException.class)
			.hasMessage("unexisting_file.txt");
	}

}
