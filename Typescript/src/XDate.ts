export class XDate {
	private readonly date: Date;

	constructor(yyyyMMdd?: string) {
		if (yyyyMMdd === undefined) {
			this.date = new Date();
			return;
		}
		const match = yyyyMMdd.match(/^(\d{4})\/(\d{2})\/(\d{2})$/);
		if (!match) {
			throw new Error(`Invalid date format, expected yyyy/MM/dd: ${yyyyMMdd}`);
		}
		const year = Number(match[1]);
		const month = Number(match[2]);
		const day = Number(match[3]);
		const parsed = new Date(year, month - 1, day);
		if (
			parsed.getFullYear() !== year ||
			parsed.getMonth() !== month - 1 ||
			parsed.getDate() !== day
		) {
			throw new Error(`Invalid date: ${yyyyMMdd}`);
		}
		this.date = parsed;
	}

	getDay(): number {
		return this.date.getDate();
	}

	getMonth(): number {
		return this.date.getMonth() + 1;
	}

	isSameDay(anotherDate: XDate): boolean {
		return anotherDate.getDay() === this.getDay() && anotherDate.getMonth() === this.getMonth();
	}

	equals(other: XDate): boolean {
		return this.date.getTime() === other.date.getTime();
	}

	toString(): string {
		const y = this.date.getFullYear();
		const m = String(this.date.getMonth() + 1).padStart(2, '0');
		const d = String(this.date.getDate()).padStart(2, '0');
		return `${y}-${m}-${d}`;
	}
}
