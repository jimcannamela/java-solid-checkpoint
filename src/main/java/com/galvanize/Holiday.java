package com.galvanize;

import java.time.Duration;
import java.time.LocalDateTime;

public class Holiday extends ICalendarItem implements Scheduable {
	private final String title;
	private final LocalDateTime date;
	public Holiday(String title, LocalDateTime date) {
		this.title = title;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}
	public LocalDateTime getStartTime() { return date; }

	public String getTextToDisplay() {
		return getTitle();
	}

	@Override
	public void markComplete() {

	}

	@Override
	public void markIncomplete() {

	}

	@Override
	public boolean isComplete() {return false;}

	@Override
	public String iCalendar() {
		if (title == null) return "";

		return new StringBuilder()
				.append("BEGIN:VEVENT\n")
				.append(String.format("DTSTART:%s\n", getStartTime()))
				.append(String.format("UID:%s@example.com\n", getUuid()))
				.append(String.format("DESCRIPTION:%s\n", getTextToDisplay()))
				.append("END:VEVENT\n")
				.toString();
	}
}