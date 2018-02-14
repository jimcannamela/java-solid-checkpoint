package com.galvanize;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class ReminderTest {

    @Test
    public void testICalendar() {
        Reminder reminder = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        String expected = "BEGIN:VALARM\n" +
                "TRIGGER:-2028-06-07T06:09\n" +
                "ACTION:DISPLAY\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "DESCRIPTION:Buy birthday hats\n" +
                "END:VALARM\n";

        assertLinesMatch(Arrays.asList(expected.split("\n")), Arrays.asList(reminder.iCalendar().split("\n")));
    }

    @Test
    public void toStringWorks() {

        Reminder reminder = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        assertEquals(
                "Buy birthday hats at Jun 7, 2028 6:09 AM (incomplete)",
                reminder.toString()
        );

        reminder.markComplete();

        assertEquals(
                "Buy birthday hats at Jun 7, 2028 6:09 AM (complete)",
                reminder.toString()
        );
    }

}
