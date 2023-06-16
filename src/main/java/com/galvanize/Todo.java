package com.galvanize;

import java.time.LocalDateTime;

public class Todo extends ICalendarItem implements Completable{

    private final String text;
    private String description;
    private TodoStatus status = TodoStatus.INCOMPLETE;
    private LocalDateTime completedAt;

    private Owner owner;

    public Owner getOwner() {
        return owner;
    }
    public Todo(String text, Owner owner) {
        this.text = text;
        this.owner = owner;
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
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail(),
                owner.getJobTitle(),
                status == TodoStatus.INCOMPLETE ? "incomplete" : "complete"
        );
    }

}
