import * as fs from 'fs';
import * as nodemailer from 'nodemailer';
import { Employee } from './Employee';
import { XDate } from './XDate';

export class BirthdayService {

	async sendGreetings(fileName: string, xDate: XDate, smtpHost: string, smtpPort: number): Promise<void> {
		const content = fs.readFileSync(fileName, 'utf-8');
		const lines = content.split('\n');
		// skip header (first line) and ignore trailing empty lines
		for (let i = 1; i < lines.length; i++) {
			const line = lines[i];
			if (line === '') continue;
			const employeeData = line.split(', ');
			const employee = new Employee(employeeData[1], employeeData[0], employeeData[2], employeeData[3]);
			if (employee.isBirthday(xDate)) {
				const recipient = employee.getEmail();
				const body = 'Happy Birthday, dear %NAME%'.replace('%NAME%', employee.getFirstName());
				const subject = 'Happy Birthday!';
				const transporter = nodemailer.createTransport({ host: smtpHost, port: smtpPort, secure: false, ignoreTLS: true });
				await transporter.sendMail({ from: 'sender@here.com', to: recipient, subject, text: body });
				transporter.close();
			}
		}
	}

}
