import { describe, it, expect } from 'vitest';
import { Employee } from '../src/Employee';
import { XDate } from '../src/XDate';

describe('EmployeeTest', () => {
	it('testBirthday', () => {
		const employee = new Employee('foo', 'bar', '1990/01/31', 'a@b.c');

		expect(employee.isBirthday(new XDate('2008/01/30')), 'not his birthday').toBe(false);
		expect(employee.isBirthday(new XDate('2008/01/31')), 'his birthday').toBe(true);
	});
});
