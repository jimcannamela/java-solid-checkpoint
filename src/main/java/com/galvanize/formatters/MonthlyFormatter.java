package com.galvanize.formatters;

import com.galvanize.Calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.*;

public class MonthlyFormatter {

    public String format(Calendar calendar) {
        if (calendar.getFirstDateTime() == null) return "";

        LocalDate startMonth = calendar.getFirstDateTime().toLocalDate().with(firstDayOfMonth());
        LocalDate endMonth = calendar.getLastDateTime().toLocalDate().with(lastDayOfMonth());

        Map<Month, List<LocalDate>> monthListMap = Stream.iterate(startMonth, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startMonth, endMonth))
                .collect(
                        groupingBy(
                                LocalDate::getMonth,
                                LinkedHashMap::new,
                                mapping(Function.identity(), toList())
                        ));

        StringBuilder builder = new StringBuilder();

        monthListMap.keySet().forEach(month -> {
            builder.append(month.getDisplayName(TextStyle.FULL, Locale.US)).append("\n");
            int previousDay = 0;

            builder.append(String.join(" ", Collections.nCopies(monthListMap.get(month).get(0).getDayOfWeek().getValue() - 1, "   ")));

            for (LocalDate date : monthListMap.get(month)) {
                if (date.getDayOfWeek().getValue() < previousDay) {
                    builder.append("\n");
                } else {
                    builder.append(" ");
                }
                int dayOfMonth = date.getDayOfMonth();
                String displayDay = String.valueOf(dayOfMonth);
                if (displayDay.length() == 1) displayDay = " " + displayDay;
                builder.append(displayDay);

                if (!calendar.descriptionsFor(date).isEmpty()) {
                    builder.append("*");
                } else {
                    builder.append(" ");
                }
                previousDay = date.getDayOfWeek().getValue();
            }

            builder.append("\n\n");
        });

        return builder.toString();
    }
}
