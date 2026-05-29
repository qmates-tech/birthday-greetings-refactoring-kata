package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileEmployeeRepository implements EmployeeRepository {

	private final String fileName;

	public FileEmployeeRepository(String fileName) {
		this.fileName = fileName;
	}

	public List<Employee> getAll() throws IOException {
		List<Employee> result = new ArrayList<>();

		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String str = "";
		str = in.readLine(); // skip header
		while ((str = in.readLine()) != null) {
			String[] employeeData = str.split(", ");
			Employee employee = new Employee(employeeData[1], employeeData[0], employeeData[2], employeeData[3]);
			result.add(employee);
		}

		return result;
	}

}
