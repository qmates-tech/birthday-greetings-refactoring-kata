import { XDate } from './XDate';

export class Employee {
	private readonly firstName: string;
	private readonly lastName: string;
	private readonly birthDate: XDate;
	private readonly email: string;

	constructor(firstName: string, lastName: string, birthDate: string, email: string) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = new XDate(birthDate);
		this.email = email;
	}

	isBirthday(today: XDate): boolean {
		return today.isSameDay(this.birthDate);
	}

	getEmail(): string {
		return this.email;
	}

	getFirstName(): string {
		return this.firstName;
	}

	toString(): string {
		return `Employee ${this.firstName} ${this.lastName} <${this.email}> born ${this.birthDate}`;
	}
}
