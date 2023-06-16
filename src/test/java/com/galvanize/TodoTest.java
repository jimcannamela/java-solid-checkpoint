package com.galvanize;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class TodoTest {

    @Test
    public void testICalendar() {
        Owner owner = new Owner("Alex", "Hamilton", "alex@example.com", "Treasurer");

        Todo todo = new Todo("Do stuff", owner);

        String expected = "BEGIN:VTODO\n" +
                "COMPLETED::null\n" +
                "UID:[a-z0-9-]{36}@example.com\n" +
                "SUMMARY:Do stuff\n" +
                "END:VTODO\n";

        assertLinesMatch(Arrays.asList(expected.split("\n")), Arrays.asList(todo.iCalendar().split("\n")));
    }

    @Test
    public void toStringWorks() {
        Owner owner = new Owner("Alex", "Hamilton", "alex@example.com", "Treasurer");

        Todo todo = new Todo("Do stuff", owner);

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
        Owner owner = new Owner("Alex", "Hamilton", "alex@example.com", "Treasurer");

        Todo todo = new Todo("Do stuff", owner);
        todo.setDescription("There's a million things he hasn't done");

        assertEquals(
                "There's a million things he hasn't done",
                todo.getDescription()
        );
    }

}
