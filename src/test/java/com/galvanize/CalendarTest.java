package com.galvanize;

import com.galvanize.formatters.MonthlyFormatter;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarTest {

    @Test
    public void itSchedulesThingsCorrectly() {
        Duration twoHours = Duration.of(2, ChronoUnit.HOURS);
        Event event = new Event(
                "My birthday party",
                LocalDateTime.of(2028, 6, 7, 8, 9),
                twoHours);

        Reminder reminder1 = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        Reminder reminder2 = new Reminder(
                "Clean up house",
                LocalDateTime.of(2028, 6, 8, 6, 9));

        Calendar calendar = new Calendar();

        calendar.addSchedulable(event);
        calendar.addSchedulable(reminder1);
        calendar.addSchedulable(reminder2);

        assertEquals(Arrays.asList(reminder1, event, reminder2), calendar.items());
    }

    @Test
    public void itFormatsThingsCorrectly() {
        Duration twoHours = Duration.of(2, ChronoUnit.HOURS);
        Event event = new Event(
                "My birthday party",
                LocalDateTime.of(2028, 6, 7, 8, 9),
                twoHours);

        Reminder reminder1 = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        Calendar calendar = new Calendar();

        calendar.addSchedulable(event);
        calendar.addSchedulable(reminder1);

        String expected = "June\n" +
                "             1   2   3   4 \n" +
                " 5   6   7*  8   9  10  11 \n" +
                "12  13  14  15  16  17  18 \n" +
                "19  20  21  22  23  24  25 \n" +
                "26  27  28  29 \n" +
                "\n";
        MonthlyFormatter mf = new MonthlyFormatter();
        assertEquals(expected, calendar.format(mf));
    }

}
