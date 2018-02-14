package com.galvanize.formatters;

import com.galvanize.Calendar;
import com.galvanize.Event;
import com.galvanize.Reminder;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleFormatterTest {

    @Test
    public void itFormatsTheCalendar() {
        Calendar calendar = new Calendar();
        Duration defaultDuration = Duration.of(1, HOURS);

        calendar.addSchedulable(new Event("Event 1", LocalDateTime.of(2017, 1, 3, 4, 4), defaultDuration));
        calendar.addSchedulable(new Event("Event 2", LocalDateTime.of(2017, 1, 3, 5, 5), defaultDuration));
        calendar.addSchedulable(new Reminder("Reminder 1", LocalDateTime.of(2017, 2, 3, 4, 4)));

        String expected = "2017-01-03\n" +
                " - Event 1 at Jan 3, 2017 4:04 AM (ends at Jan 3, 2017 5:04 AM)\n" +
                " - Event 2 at Jan 3, 2017 5:05 AM (ends at Jan 3, 2017 6:05 AM)\n" +
                "\n" +
                "2017-02-03\n" +
                " - Reminder 1 at Feb 3, 2017 4:04 AM (incomplete)\n\n";

        ScheduleFormatter formatter = new ScheduleFormatter();
        assertEquals(expected, formatter.format(calendar));
    }

}
