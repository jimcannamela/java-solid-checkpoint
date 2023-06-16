package com.galvanize;

import java.time.LocalDateTime;

public class Reminder extends ICalendarItem implements Scheduable, Completable {

    private final String description;
    private final LocalDateTime remindsAt;
    private boolean complete;

    public Reminder(String description, LocalDateTime remindsAt) {
        this.description = description;
        this.remindsAt = remindsAt;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getTextToDisplay() {
        return getDescription();
    }

    public LocalDateTime getStartTime() {
        return remindsAt;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public void markComplete() {
        this.complete = true;
    }

    @Override
    public void markIncomplete() {
        this.complete = false;
    }

    @Override
    public String iCalendar() {
        if (description == null) throw new IllegalArgumentException("You must specify a description");

        return new StringBuilder()
                .append("BEGIN:VALARM\n")
                .append(String.format("TRIGGER:-%s\n", getStartTime()))
                .append("ACTION:DISPLAY\n")
                .append(String.format("UID:%s@example.com\n", getUuid()))
                .append(String.format("DESCRIPTION:%s\n", getTextToDisplay()))
                .append("END:VALARM\n")
                .toString();
    }

    @Override
    public String toString() {
        return String.format(
                "%s at %s (%s)",
                getDescription(),
                getStartTime().format(DATE_FORMATTER),
                isComplete() ? "complete" : "incomplete"
        );
    }
}
