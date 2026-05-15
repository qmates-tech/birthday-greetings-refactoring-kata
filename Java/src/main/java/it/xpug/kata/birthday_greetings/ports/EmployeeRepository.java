package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.Employee;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EmployeeRepository {
	List<Employee> getAll() throws IOException, ParseException;
}
