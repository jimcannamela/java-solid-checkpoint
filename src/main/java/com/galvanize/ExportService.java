package com.galvanize;

import java.util.List;

public class ExportService {

    public String export(List<ICalendarItem> objectsToExport) {
        StringBuilder builder = new StringBuilder()
                .append("BEGIN:VCALENDAR\n")
                .append("VERSION:2.0\n");

        objectsToExport.forEach(object -> {
            builder.append(object.iCalendar());
        });

        return builder
                .append("END:VCALENDAR\n")
                .toString();
    }

}
