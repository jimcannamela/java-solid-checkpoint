package com.galvanize;

import com.galvanize.formatters.MonthlyFormatter;
import com.galvanize.formatters.ScheduleFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

public class Application {

    public static void main(String[] args) {
        Duration defaultDuration = Duration.of(1, HOURS);
        Event event1 = new Event("Event 1", LocalDateTime.of(2017, 1, 3, 4, 4), defaultDuration);
        Event event2 = new Event("Event 2", LocalDateTime.of(2017, 1, 3, 5, 5), defaultDuration);
        Event event3 = new Event("Event 2", LocalDateTime.of(2017, 1, 12, 5, 5), defaultDuration);
        Reminder reminder1 = new Reminder("Reminder 1", LocalDateTime.of(2017, 1, 17, 4, 4));
        reminder1.markComplete();
        Owner owner = new Owner("Alex", "Hamilton", "alex@example.com", "Treasurer");
        Todo todo1 = new Todo("Do stuff", owner);

        // this shows how the calendar and calendar formatters work
        MonthlyFormatter monthlyFormatter = new MonthlyFormatter();
        ScheduleFormatter scheduleFormatter = new ScheduleFormatter();

        Calendar calendar = new Calendar();
        calendar.addSchedulable(event1);
        calendar.addSchedulable(event2);
        calendar.addSchedulable(reminder1);
        calendar.addSchedulable(event3);

        System.out.println("------------------\nCalendar View:\n------------------\n");
        System.out.println(monthlyFormatter.format(calendar));

        System.out.println("------------------\nSchedule View:\n------------------\n");
        System.out.println(scheduleFormatter.format(calendar));

        // this shows how the TodoList works
        System.out.println("-------------------\nTodo List:\n-------------------\n");
        TodoList todoList = new TodoList();
        todoList.add(todo1);
        todoList.add(reminder1);
        System.out.println(todoList.toString());

        // this shows how the export service works
        System.out.println("-------------------\niCalendar Export:\n-------------------\n");
        List<ICalendarItem> items = Arrays.asList(event1, event2, todo1, reminder1, event3);
        ExportService exporter = new ExportService();
        System.out.println(exporter.export(items));

    }

}
