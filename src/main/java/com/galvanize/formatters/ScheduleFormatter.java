package com.galvanize.formatters;

import com.galvanize.Calendar;

public class ScheduleFormatter implements Formattable {

    public String format(Calendar calendar) {
        StringBuilder builder = new StringBuilder();
        calendar.dates().forEach(date -> {
            builder.append(date).append("\n");
            calendar.descriptionsFor(date).forEach(description -> {
                builder.append(" - ").append(description).append("\n");
            });
            builder.append("\n");
        });

        return builder.toString();
    }
}
