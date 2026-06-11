package it.xpug.kata.birthday_greetings.ports;

import it.xpug.kata.birthday_greetings.XDate;

import java.time.LocalDate;

public class RealXCalendar implements XCalendar {

	@Override
	public XDate today() {
		return new XDate(LocalDate.now());
	}

}
