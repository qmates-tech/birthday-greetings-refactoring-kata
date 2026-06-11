package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.XDate;

public class FakeXCalendar implements XCalendar {

	private XDate todayDate;

	@Override
	public XDate today() {
		return this.todayDate;
	}

	public void setTodayDate(XDate todayDate) {
		this.todayDate = todayDate;
	}
}
