package com.galvanize.formatters;

import com.galvanize.Calendar;
import com.galvanize.Event;
import com.galvanize.Reminder;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonthlyFormatterTest {

    @Test
    public void itFormatsTheCalendar() {
        Calendar calendar = new Calendar();
        Duration defaultDuration = Duration.of(1, HOURS);

        calendar.addSchedulable(new Event("Event 1", LocalDateTime.of(2017, 1, 3, 4, 4), defaultDuration));
        calendar.addSchedulable(new Event("Event 2", LocalDateTime.of(2017, 1, 3, 5, 5), defaultDuration));
        calendar.addSchedulable(new Reminder("Reminder 1", LocalDateTime.of(2017, 2, 3, 4, 4)));

        String expected = "January\n" +
                "                         1 \n" +
                " 2   3*  4   5   6   7   8 \n" +
                " 9  10  11  12  13  14  15 \n" +
                "16  17  18  19  20  21  22 \n" +
                "23  24  25  26  27  28  29 \n" +
                "30  31 \n" +
                "\n" +
                "February\n" +
                "         1   2   3*  4   5 \n" +
                " 6   7   8   9  10  11  12 \n" +
                "13  14  15  16  17  18  19 \n" +
                "20  21  22  23  24  25  26 \n" +
                "27 \n" +
                "\n";

        MonthlyFormatter formatter = new MonthlyFormatter();
        assertEquals(expected, formatter.format(calendar));
    }

}
