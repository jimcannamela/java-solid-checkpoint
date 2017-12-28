package com.galvanize;

import com.galvanize.formatters.MonthlyFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Calendar {

    private final List<Object> schedulables = new ArrayList<>();

    public void addSchedulable(Object schedulable) {
        schedulables.add(schedulable);
        schedulables.sort((a, b) -> {
            LocalDateTime dateA = getLocalDateTime(a);
            LocalDateTime dateB = getLocalDateTime(b);
            return dateA.compareTo(dateB);
        });
    }

    public List<Object> items() {
        return schedulables;
    }

    public List<LocalDate> dates() {
        return schedulables
                .stream()
                .map(this::getLocalDateTime)
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .collect(toList());
    }

    public List<String> descriptionsFor(LocalDate date) {
        return schedulables.stream()
                .filter(item -> getLocalDateTime(item).toLocalDate().equals(date))
                .map(Object::toString)
                .collect(toList());
    }

    public LocalDateTime getFirstDateTime() {
        if (schedulables.isEmpty()) return null;
        Object item = schedulables.get(0);
        return getLocalDateTime(item);
    }

    public LocalDateTime getLastDateTime() {
        if (schedulables.isEmpty()) return null;
        Object item = schedulables.size() == 1 ? schedulables.get(0) : schedulables.get(schedulables.size() - 1);
        return getLocalDateTime(item);
    }

    public String format() {
        MonthlyFormatter formatter = new MonthlyFormatter();
        return formatter.format(this);
    }

    private LocalDateTime getLocalDateTime(Object item) {
        if (item instanceof Event) {
            return ((Event) item).getStartsAt();
        } else if (item instanceof Reminder) {
            return ((Reminder) item).getRemindsAt();
        }
        return null;
    }

}
