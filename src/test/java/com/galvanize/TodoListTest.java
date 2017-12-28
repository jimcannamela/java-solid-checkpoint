package com.galvanize;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoListTest {

    @Test
    public void itManagesCompletablesCorrectly() {
        Reminder reminder = new Reminder(
                "Buy birthday hats",
                LocalDateTime.of(2028, 6, 7, 6, 9));

        Todo todo = new Todo("Do stuff", "Alex", "Hamilton", "alex@example.com", "Treasurer");

        TodoList todoList = new TodoList();

        todoList.add(reminder);
        todoList.add(todo);

        assertEquals(Arrays.asList(reminder, todo), todoList.all());
        assertEquals(Arrays.asList(reminder, todo), todoList.uncompleted());
        assertEquals(Collections.emptyList(), todoList.completed());

        reminder.markComplete();

        assertEquals(Arrays.asList(reminder, todo), todoList.all());
        assertEquals(Arrays.asList(todo), todoList.uncompleted());
        assertEquals(Arrays.asList(reminder), todoList.completed());

        todoList.completeAll();

        assertEquals(Arrays.asList(reminder, todo), todoList.all());
        assertEquals(Collections.emptyList(), todoList.uncompleted());
        assertEquals(Arrays.asList(reminder, todo), todoList.completed());

        todoList.uncompleteAll();

        assertEquals(Arrays.asList(reminder, todo), todoList.uncompleted());
        assertEquals(Collections.emptyList(), todoList.completed());

    }

}
