import { describe, it, expect } from 'vitest';
import { XDate } from '../src/XDate';

describe('XDateTest', () => {
	it('getters', () => {
		const date = new XDate('1789/01/24');

		expect(date.getMonth()).toBe(1);
		expect(date.getDay()).toBe(24);
	});

	it('isSameDate', () => {
		const date = new XDate('1789/01/24');
		const sameDay = new XDate('2001/01/24');
		const notSameDay = new XDate('1789/01/25');
		const notSameMonth = new XDate('1789/02/25');

		expect(date.isSameDay(sameDay), 'same').toBe(true);
		expect(date.isSameDay(notSameDay), 'not same day').toBe(false);
		expect(date.isSameDay(notSameMonth), 'not same month').toBe(false);
	});
});
