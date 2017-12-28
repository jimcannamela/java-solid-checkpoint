package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import com.galvanize.util.ReflectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.galvanize.OpenClosedTest.EVENT_CLASS_NAME;
import static com.galvanize.OpenClosedTest.REMINDER_CLASS_NAME;
import static com.galvanize.SingleResponsibilityTest.TODO_CLASS_NAME;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Liskov Substitution Principle")
public class LiskovTest {

    public static final String EXPORT_SERVICE_CLASS_NAME = "com.galvanize.ExportService";
    public static final String I_CALENDAR_ITEM_CLASS_NAME = "com.galvanize.ICalendarItem";
    public static final String EXPORT_METHOD_NAME = "export";

    @BeforeAll
    public static void verifyInitialState() {
        try {
            new ClassProxy(TODO_CLASS_NAME);
            new ClassProxy(EVENT_CLASS_NAME);
            new ClassProxy(REMINDER_CLASS_NAME);
            new ClassProxy(I_CALENDAR_ITEM_CLASS_NAME);
            new ClassProxy(EXPORT_SERVICE_CLASS_NAME);
        } catch (AssertionFailedError e) {
            fail(e.getMessage() + ". Did you move, delete or rename it by mistake?");
        }
    }

    @Test
    @DisplayName("Subclasses should be interchangeable with base classes")
    public void aTest() throws Throwable {
        ClassProxy _Event = new ClassProxy(EVENT_CLASS_NAME)
                .ensureConstructor(String.class, LocalDateTime.class, Duration.class);
        Object event = _Event.newInstance(null, LocalDateTime.of(2017, 1, 3, 4, 4), Duration.of(1, HOURS)).getDelegate();

        ClassProxy _Reminder = new ClassProxy(REMINDER_CLASS_NAME)
                .ensureConstructor(String.class, LocalDateTime.class);
        Object reminder = _Reminder.newInstance(null, LocalDateTime.of(2017, 3, 17, 4, 4)).getDelegate();

        Object todo = constructTodo(null, "Alex", "Hamilton", "alex@example.com", "Treasurer");

        boolean eventThrew = false;
        boolean reminderThrew = false;
        boolean todoThrew = false;

        Object eventResult = "";
        Object reminderResult = "";
        Object todoResult = "";

        ClassProxy _ICalendarItem = new ClassProxy(I_CALENDAR_ITEM_CLASS_NAME);
        ClassProxy _ExportService = new ClassProxy(EXPORT_SERVICE_CLASS_NAME)
                .ensureMethod(EXPORT_METHOD_NAME, ReflectionUtils.listOf(_ICalendarItem));
        InstanceProxy exporter = _ExportService.newInstance();
        try {
            eventResult = exporter.invokeExpectingException(EXPORT_METHOD_NAME, singletonList(event));
        } catch (IllegalArgumentException e) {
            eventThrew = true;
        }

        try {
            reminderResult = exporter.invokeExpectingException(EXPORT_METHOD_NAME, singletonList(reminder));
        } catch (IllegalArgumentException e) {
            reminderThrew = true;
        }

        try {
            todoResult = exporter.invokeExpectingException(EXPORT_METHOD_NAME, singletonList(todo));
        } catch (IllegalArgumentException e) {
            todoThrew = true;
        }

        Stream<Boolean> subclasses = Stream.of(eventThrew, reminderThrew, todoThrew);
        if (subclasses.distinct().count() != 1) {
            fail("The classes `Event`, `Reminder` and `Todo` do not have the same preconditions in the `iCalendar` method.");
        }

        if (!eventThrew && !reminderThrew && !todoThrew) {
            Stream<Object> results = Stream.of(eventResult, reminderResult, todoResult);
            if (results.distinct().count() != 1) {
                fail("The `iCalendar` method of `Event`, `Reminder` and `Todo` return different results when their display text field returns `null`");
            }
        }

    }

    private Object constructTodo(String description, String firstName, String lastName, String email, String title) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object todoInstance = null;

        ClassProxy _Todo = new ClassProxy(TODO_CLASS_NAME).ensureConstructorCount(1);
        Constructor todoConstructor = Arrays.stream(_Todo.getDelegate().getConstructors()).findFirst().get();
        Class[] paramTypes = todoConstructor.getParameterTypes();
        if (paramTypes.length == 5) {
            todoInstance = todoConstructor.newInstance(description, firstName, lastName, email, title);
        } else {
            Class ownerClass;
            boolean stringFirst = false;
            if (paramTypes[0] == String.class) {
                stringFirst = true;
                ownerClass = paramTypes[1];
            } else {
                ownerClass = paramTypes[0];
            }
            ClassProxy _Owner = new ClassProxy(ownerClass)
                    .ensureConstructor(String.class, String.class, String.class, String.class);
            InstanceProxy owner = _Owner.newInstance(firstName, lastName, email, title);
            if (stringFirst) {
                todoInstance = todoConstructor.newInstance(description, owner.getDelegate());
            } else {
                todoInstance = todoConstructor.newInstance(owner.getDelegate(), description);
            }
        }

        return todoInstance;
    }

}
