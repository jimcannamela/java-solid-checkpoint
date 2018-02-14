package com.galvanize;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class ICalendarItem implements Completable {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, y h:mm a");
    private String uuid;

    public ICalendarItem() {
        super();
        uuid = UUID.randomUUID().toString();
    }

    protected String getUuid() {
        return uuid;
    }

    public abstract String iCalendar();

}
