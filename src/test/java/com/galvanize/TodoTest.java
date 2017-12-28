package com.galvanize;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class TodoTest {

    @Test
    public void testICalendar() {
        Todo todo = new Todo("Do stuff", "Alex", "Hamilton", "alex@example.com", "Treasurer");

        String expected = "BEGIN:VTODO\n" +
                "COMPLETED::null\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "SUMMARY:Do stuff\n" +
                "END:VTODO\n";

        assertLinesMatch(Arrays.asList(expected.split("\n")), Arrays.asList(todo.iCalendar().split("\n")));
    }

    @Test
    public void toStringWorks() {
        Todo todo = new Todo("Do stuff", "Alex", "Hamilton", "alex@example.com", "Treasurer");

        assertEquals(
                "Do stuff <Alex Hamilton> alex@example.com (Treasurer): incomplete",
                todo.toString()
        );

        todo.markComplete();

        assertEquals(
                "Do stuff <Alex Hamilton> alex@example.com (Treasurer): complete",
                todo.toString()
        );
    }

    @Test
    public void itHasADescription() {
        Todo todo = new Todo("Do stuff", "Alex", "Hamilton", "alex@example.com", "Treasurer");
        todo.setDescription("There's a million things he hasn't done");

        assertEquals(
                "There's a million things he hasn't done",
                todo.getDescription()
        );
    }

}
