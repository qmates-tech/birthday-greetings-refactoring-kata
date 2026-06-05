import { BirthdayService } from './BirthdayService';
import { XDate } from './XDate';

async function main(): Promise<void> {
	const service = new BirthdayService();
	await service.sendGreetings('employee_data.txt', new XDate(), 'localhost', 25);
}

main().catch((err) => {
	console.error(err);
	process.exit(1);
});
