package com.galvanize;

import java.time.LocalDateTime;

public class Todo extends ICalendarItem {

    private final String text;
    private String description;
    private TodoStatus status = TodoStatus.INCOMPLETE;
    private LocalDateTime completedAt;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerEmail;
    private String ownerJobTitle;

    public Todo(String text, String ownerFirstName, String ownerLastName, String ownerEmail, String ownerJobTitle) {
        this.text = text;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerEmail = ownerEmail;
        this.ownerJobTitle = ownerJobTitle;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTextToDisplay() {
        return getText();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void markComplete() {
        status = TodoStatus.COMPLETE;
        completedAt = LocalDateTime.now();
    }

    @Override
    public void markIncomplete() {
        status = TodoStatus.INCOMPLETE;
        completedAt = null;
    }

    @Override
    public boolean isComplete() {
        return status == TodoStatus.COMPLETE;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public String getOwnerJobTitle() {
        return ownerJobTitle;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    @Override
    public String iCalendar() {
        if (text == null) throw new IllegalArgumentException("You must specify the text");

        return new StringBuilder()
                .append("BEGIN:VTODO\n")
                .append(String.format("COMPLETED::%s\n", getCompletedAt()))
                .append(String.format("UID:%s@example.com\n", getUuid()))
                .append(String.format("SUMMARY:%s\n", getTextToDisplay()))
                .append("END:VTODO\n")
                .toString();
    }

    @Override
    public String toString() {
        return String.format(
                "%s <%s %s> %s (%s): %s",
                getText(),
                getOwnerFirstName(),
                getOwnerLastName(),
                getOwnerEmail(),
                getOwnerJobTitle(),
                status == TodoStatus.INCOMPLETE ? "incomplete" : "complete"
        );
    }

}
