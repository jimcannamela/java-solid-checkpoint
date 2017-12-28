package com.galvanize;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class EventTest {

    @Test
    public void testICalendar() {
        Duration twoHours = Duration.of(2, ChronoUnit.HOURS);
        Event event = new Event(
                "My birthday party",
                LocalDateTime.of(2028, 6, 7, 8, 9),
                twoHours);

        String expected = "BEGIN:VEVENT\n" +
                "DTSTART:2028-06-07T08:09\n" +
                "DTEND:2028-06-07T10:09\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "DESCRIPTION:My birthday party\n" +
                "END:VEVENT\n";

        assertLinesMatch(Arrays.asList(expected.split("\n")), Arrays.asList(event.iCalendar().split("\n")));
    }

    @Test
    public void toStringWorks() {
        Duration twoHours = Duration.of(2, ChronoUnit.HOURS);
        Event event = new Event(
                "My birthday party",
                LocalDateTime.of(2028, 6, 7, 8, 9),
                twoHours);

        assertEquals("My birthday party at Jun 7, 2028 8:09:00 AM (ends at Jun 7, 2028 10:09:00 AM)", event.toString());
    }

}
