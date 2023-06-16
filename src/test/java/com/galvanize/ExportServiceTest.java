package com.galvanize;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class ExportServiceTest {

    @Test
    public void itExportsEverything() {
        Duration twoHours = Duration.of(2, ChronoUnit.HOURS);
        Event event = new Event(
                "My birthday party",
                LocalDateTime.of(2028, 6, 7, 8, 9),
                twoHours);

        Reminder reminder = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        Owner owner = new Owner("Alex", "Hamilton", "alex@example.com", "Treasurer");

        Todo todo = new Todo("Do stuff", owner);

        String expected = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "DTSTART:2028-06-07T08:09\n" +
                "DTEND:2028-06-07T10:09\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "DESCRIPTION:My birthday party\n" +
                "END:VEVENT\n" +
                "BEGIN:VALARM\n" +
                "TRIGGER:-2028-06-07T06:09\n" +
                "ACTION:DISPLAY\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "DESCRIPTION:Buy birthday hats\n" +
                "END:VALARM\n" +
                "BEGIN:VTODO\n" +
                "COMPLETED::null\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "SUMMARY:Do stuff\n" +
                "END:VTODO\n" +
                "END:VCALENDAR\n";

        String actual = new ExportService().export(Arrays.asList(event, reminder, todo));
        assertLinesMatch(Arrays.asList(expected.split("\n")), Arrays.asList(actual.split("\n")));
    }

}
