package com.galvanize;

import java.util.UUID;

public abstract class ICalendarItem implements Completable {

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
