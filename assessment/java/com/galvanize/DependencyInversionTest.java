package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import com.galvanize.util.MethodBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Arrays;

import static com.galvanize.OpenClosedTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Dependency Inversion Principle")
public class DependencyInversionTest {

    public static final String MONTHLY_FORMATTER_CLASS_NAME = "com.galvanize.formatters.MonthlyFormatter";
    public static final String SCHEDULE_FORMATTER_CLASS_NAME = "com.galvanize.formatters.ScheduleFormatter";
    private static String FORMATTER_RESULT = "Some hard-coded result";

    @BeforeAll
    public static void verifyInitialState() {
        try {
            new ClassProxy(CALENDAR_CLASS_NAME);
            new ClassProxy(MONTHLY_FORMATTER_CLASS_NAME);
            new ClassProxy(SCHEDULE_FORMATTER_CLASS_NAME);
            new ClassProxy(EVENT_CLASS_NAME);
            new ClassProxy(REMINDER_CLASS_NAME);
        } catch (AssertionFailedError e) {
            fail(e.getMessage() + ". Did you move, delete or rename it by mistake?");
        }
    }

    @Test
    @DisplayName("High-level modules should not depend on low-level modules")
    public void aTest() {
        Class<?> monthlyFormatterClass = new ClassProxy(MONTHLY_FORMATTER_CLASS_NAME).getDelegate();

        Class<?>[] monthlyFormatterInterfaces = monthlyFormatterClass.getInterfaces();
        if (monthlyFormatterInterfaces.length == 0) {
            fail("Expected `MonthlyFormatter` to implement at least 1 interface");
        }

        Class<?> scheduleFormatterClass = new ClassProxy(SCHEDULE_FORMATTER_CLASS_NAME).getDelegate();

        Class<?>[] scheduleFormatterInterfaces = scheduleFormatterClass.getInterfaces();
        if (scheduleFormatterInterfaces.length == 0) {
            fail("Expected `ScheduleFormatter` to implement at least 1 interface");
        }

        assertEquals(
                true,
                monthlyFormatterInterfaces[0].equals(scheduleFormatterInterfaces[0]),
                "Expected `MonthlyFormatter` and `ScheduleFormatter` to implement the same interface"
        );

        Class<?> formatterInterface = monthlyFormatterInterfaces[0];

        ClassProxy _Calendar = new ClassProxy(CALENDAR_CLASS_NAME);
        Class<?> calendarClass = _Calendar.getDelegate();

        boolean usingConstructorInjection = Arrays.stream(calendarClass.getConstructors())
                .anyMatch(constructor -> Arrays.stream(constructor.getParameterTypes())
                        .anyMatch(type -> type.equals(formatterInterface)));

        boolean usingParameterInjection = new MethodBuilder(calendarClass)
                .named("format")
                .withParameters(formatterInterface)
                .build() != null;

        if (!usingConstructorInjection && !usingParameterInjection) {
            fail(String.format(
                    "Expected `Calendar` to either inject `%s` as a constructor parameter, or a method parameter",
                    formatterInterface.getName()));
        }

        InstanceProxy formatter = ClassProxy.of(formatterInterface).subclass()
                .intercept("format", args -> String.format("%s %s", FORMATTER_RESULT, args[0]))
                .build();

        if (usingConstructorInjection) {
            testConstructorInjection(_Calendar, formatterInterface, formatter);
        } else {
            testParameterInjection(_Calendar, formatterInterface, formatter);
        }

    }

    private void testParameterInjection(ClassProxy _Calendar, Class<?> formatterClass, InstanceProxy formatterInstance) {

        _Calendar.ensureMethod("format", formatterClass);

        InstanceProxy calendar = _Calendar.newInstance();
        String result = (String) calendar.invoke("format", formatterInstance);
        if (!result.contains(FORMATTER_RESULT)) {
            fail("Expected `Calendar.format` to return the results of the injected formatter, but it did not");
        }
        if (!result.contains(calendar.getDelegate().toString())) {
            fail(String.format(
                    "Expected `Calendar.format` to pass the instance of calendar to the %s, but it did not",
                    formatterClass.getName()
            ));
        }
    }

    private void testConstructorInjection(ClassProxy _Calendar, Class<?> formatterClass, InstanceProxy formatterInstance) {

        _Calendar
                .ensureConstructor(formatterClass)
                .ensureMethod("format");

        InstanceProxy calendar = _Calendar.newInstance(formatterInstance);
        String result = (String) calendar.invoke("format");
        if (!result.contains(FORMATTER_RESULT)) {
            fail("Expected `Calendar.format` to return the results of the injected formatter, but it did not");
        }
        if (!result.contains(calendar.getDelegate().toString())) {
            fail(String.format(
                    "Expected `Calendar.format` to pass the instance of calendar to the `%s`, but it did not",
                    formatterClass.getName()
            ));
        }
    }

}
