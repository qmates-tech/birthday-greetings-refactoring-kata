import { describe, beforeEach, afterEach, it, expect } from 'vitest';
import { SMTPServer } from 'smtp-server';
import { simpleParser, ParsedMail, AddressObject } from 'mailparser';
import { BirthdayService } from '../src/BirthdayService';
import { XDate } from '../src/XDate';

const NONSTANDARD_PORT = 9999;

describe('AcceptanceTest', () => {
	let birthdayService: BirthdayService;
	let mailServer: SMTPServer;
	let receivedMessages: ParsedMail[];

	beforeEach(async () => {
		receivedMessages = [];
		mailServer = await setupTestMailServer(mailServer, receivedMessages);
		birthdayService = new BirthdayService();
	});

	afterEach(async () => {
		await new Promise<void>((resolve) => mailServer.close(() => resolve()));
	});

	it('willSendGreetings_whenItsSomebodysBirthday', async () => {
		await birthdayService.sendGreetings('employee_data.txt', new XDate('2008/10/08'), 'localhost', NONSTANDARD_PORT);

		expect(receivedMessages.length, 'message not sent?').toBe(1);

		const message = receivedMessages[0];
		expect(getBody(message)).toBe('Happy Birthday, dear John!');
		expect(message.subject).toBe('Happy Birthday!');

		const recipients = getRecipients(message);
		expect(recipients).toHaveLength(1);
		expect(recipients[0]).toBe('john.doe@foobar.com');
	});

	it('willNotSendEmailsWhenNobodysBirthday', async () => {
		await birthdayService.sendGreetings('employee_data.txt', new XDate('2008/01/01'), 'localhost', NONSTANDARD_PORT);

		expect(receivedMessages.length, 'what? messages?').toBe(0);
	});
});

function getBody(message: ParsedMail): string {
	return (message.text ?? '').trim();
}

function getRecipients(message: ParsedMail): string[] {
	const to = message.to;
	if (!to) return [];
	const list: AddressObject[] = Array.isArray(to) ? to : [to];
	return list.flatMap((addr) => addr.value.map((v) => v.address ?? ''));
}

async function setupTestMailServer(mailServer: SMTPServer, receivedMessages: ParsedMail[]) {
	mailServer = new SMTPServer({
		authOptional: true,
		disabledCommands: ['AUTH', 'STARTTLS'],
		onData(stream, _session, callback) {
			simpleParser(stream)
				.then((parsed) => {
					receivedMessages.push(parsed);
					callback();
				})
				.catch((err) => callback(err));
		},
	});
	await new Promise<void>((resolve, reject) => {
		mailServer.on('error', reject);
		mailServer.listen(NONSTANDARD_PORT, undefined, () => resolve());
	});
	return mailServer;
}
