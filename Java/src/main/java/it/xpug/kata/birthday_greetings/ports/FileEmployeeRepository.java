package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileEmployeeRepository implements EmployeeRepository {

	private final String fileName;

	public FileEmployeeRepository(String fileName) {
		this.fileName = fileName;
	}

	public List<Employee> getAll() throws IOException {
		try (Stream<String> lines = Files.lines(Path.of(fileName))) {
			return lines
				.skip(1)
				.map(this::buildEmployeeFrom)
				.collect(Collectors.toList());
		}
	}

	private Employee buildEmployeeFrom(String fileLine) {
		String[] pieces = fileLine.split(", ");
		return new Employee(pieces[1], pieces[0], pieces[2], pieces[3]);
	}

}
