package com.galvanize;

import com.galvanize.util.ClassProxy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.galvanize.LiskovTest.I_CALENDAR_ITEM_CLASS_NAME;
import static com.galvanize.OpenClosedTest.EVENT_CLASS_NAME;
import static com.galvanize.OpenClosedTest.REMINDER_CLASS_NAME;
import static com.galvanize.SingleResponsibilityTest.TODO_CLASS_NAME;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Interface Segregation Principle")
public class InterfaceSegregationTest {

    @BeforeAll
    public static void verifyInitialState() {
        try {
            new ClassProxy(TODO_CLASS_NAME);
            new ClassProxy(EVENT_CLASS_NAME);
            new ClassProxy(REMINDER_CLASS_NAME);
            new ClassProxy(I_CALENDAR_ITEM_CLASS_NAME);
        } catch (AssertionFailedError e) {
            fail(e.getMessage() + ". Did you move, delete or rename it by mistake?");
        }
    }

    @Test
    @DisplayName("There should be several cohesive interfaces")
    public void aTest() {

        Class<?> todoClass = new ClassProxy(TODO_CLASS_NAME).getDelegate();
        HashSet<Class> todoInterfaces = new HashSet<>(Arrays.asList(todoClass.getInterfaces()));
        if (todoInterfaces.size() == 0) {
            fail("Expected `Todo` to implement at least one interface, but it implements none.");
        }

        Class<?> reminderClass = new ClassProxy(REMINDER_CLASS_NAME).getDelegate();
        HashSet<Class> reminderInterfaces = new HashSet<>(Arrays.asList(reminderClass.getInterfaces()));
        if (reminderInterfaces.size() < 2) {
            fail("Expected `Reminder` to implement at least two interfaces, but it implements " + reminderInterfaces.size());
        }

        todoInterfaces.retainAll(reminderInterfaces);
        if (todoInterfaces.size() < 1) {
            fail("Expected `Reminder` and `Todo` to both implement at least 1 common interface.");
        }

        Class completableInterface = todoInterfaces.stream().filter(_interface -> {
            Supplier<Stream<Method>> factory = () -> Arrays.stream(_interface.getDeclaredMethods());
            return factory.get().anyMatch(method -> method.getName().equals("markComplete")) &&
                    factory.get().anyMatch(method -> method.getName().equals("markIncomplete")) &&
                    factory.get().anyMatch(method -> method.getName().equals("isComplete"));
        }).findFirst().orElse(null);

        if (completableInterface == null) {
            fail("Expected `Todo` and `Reminder` to implement an interface with 3 methods:" +
                    " `markComplete`, `markIncomplete`, `isComplete`");
        }

        Class<?> eventClass = new ClassProxy(EVENT_CLASS_NAME).getDelegate();
        if (completableInterface.isAssignableFrom(eventClass)) {
            fail(String.format("`Event` should not implement `%s`", completableInterface.getName()));
        }

        Class<?> iCalendarItemClass = new ClassProxy(I_CALENDAR_ITEM_CLASS_NAME).getDelegate();
        HashSet<Class> iCalendarItemInterfaces = new HashSet<>(Arrays.asList(iCalendarItemClass.getInterfaces()));
        if (completableInterface.isAssignableFrom(iCalendarItemClass)) {
            fail(String.format("`ICalendarItem` should not implement `%s`", completableInterface.getName()));
        }

        Set<String> eventMethodNames = Arrays.stream(eventClass.getDeclaredMethods())
                .map(Method::getName)
                .collect(toSet());

        if (eventMethodNames.contains("markComplete")) {
            fail("Expected `Event` to _not_ define `markComplete`");
        }

        if (eventMethodNames.contains("markIncomplete")) {
            fail("Expected `Event` to _not_ define `markIncomplete`");
        }

        if (eventMethodNames.contains("isComplete")) {
            fail("Expected `Event` to _not_ define `isComplete`");
        }

        Class displayableInterface = iCalendarItemInterfaces.stream().filter(_interface -> {
            Supplier<Stream<Method>> factory = () -> Arrays.stream(_interface.getDeclaredMethods());
            return factory.get().anyMatch(method -> method.getName().equals("getTextToDisplay"));
        }).findFirst().orElse(null);

        if (displayableInterface == null) {
            fail("Expected `ICalendar` to implement an interface with 1 method: `getTextToDisplay`");
        }

        ClassProxy.of(completableInterface).ensureImplements(displayableInterface);
    }

}
