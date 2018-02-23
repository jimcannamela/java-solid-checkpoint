package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import com.galvanize.util.MethodBuilder;
import com.galvanize.util.ReflectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.galvanize.SingleResponsibilityTest.TODO_CLASS_NAME;
import static com.galvanize.util.ReflectionUtils.failFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Open/Closed Principle")
public class OpenClosedTest {

    public static final String CALENDAR_CLASS_NAME = "com.galvanize.Calendar";
    public static final String EVENT_CLASS_NAME = "com.galvanize.Event";
    public static final String REMINDER_CLASS_NAME = "com.galvanize.Reminder";
    public static final String ADD_SCHEDULABLE_METHOD_NAME = "addSchedulable";
    public static final String ITEMS_METHOD_NAME = "items";

    @BeforeAll
    public static void verifyInitialState() {
        try {
            new ClassProxy(CALENDAR_CLASS_NAME)
                .ensureMethod(method -> method.named(ADD_SCHEDULABLE_METHOD_NAME).withParameterCount(1))
                .ensureMethod(method -> method.named(ITEMS_METHOD_NAME).withParameterCount(0));
            new ClassProxy(EVENT_CLASS_NAME);
            new ClassProxy(REMINDER_CLASS_NAME);
        } catch (AssertionFailedError e) {
            fail(e.getMessage() + ". Did you move, delete or rename it by mistake?");
        }
    }

    @Test
    @DisplayName("Calendar should be open for extension")
    public void aTest() {

        ClassProxy _Calendar = new ClassProxy(CALENDAR_CLASS_NAME);

        _Calendar.ensureMethod(method -> method.named(ADD_SCHEDULABLE_METHOD_NAME).withParameterCount(1));
        Executable addSchedulableMethod = _Calendar.getVerifiedMethods().getFirst(ADD_SCHEDULABLE_METHOD_NAME).get();
        Parameter[] parameters = addSchedulableMethod.getParameters();
        Class<?> schedulable = parameters[0].getType();
        if (schedulable == Object.class) {
            failFormat("Expected `%s.%s` to take an interface as a parameter but it is still `Object`",
                    _Calendar.getDelegate().getSimpleName(),
                    ADD_SCHEDULABLE_METHOD_NAME);
        }
        ClassProxy _Schedulable = ClassProxy.of(schedulable).ensureInterface();

        _Calendar.ensureMethod(method -> method.named(ITEMS_METHOD_NAME).returns(ReflectionUtils.listOf(schedulable)));

        Method getLocalDateTimeMethod = new MethodBuilder(_Schedulable.getDelegate()).returns(LocalDateTime.class).build();
        _Schedulable.ensureMethod(getLocalDateTimeMethod);

        // so now create a new subclass of the interface...
        InstanceProxy schedulableInstance = _Schedulable.subclass()
                .intercept(getLocalDateTimeMethod.getName(), LocalDateTime.now())
                .build();

        InstanceProxy calendar = _Calendar.newInstance();
        try {
            calendar.invoke(ADD_SCHEDULABLE_METHOD_NAME, schedulableInstance);
            calendar.invoke(ADD_SCHEDULABLE_METHOD_NAME, schedulableInstance);
            calendar.invoke(ADD_SCHEDULABLE_METHOD_NAME, schedulableInstance);
        } catch (AssertionFailedError e) {
            if (e.getMessage().contains(NullPointerException.class.getName())) {
                failFormat("Calling `Calendar.addSchedulable` threw a `NullPointerException` when passed a new class" +
                                " that implements `%s`.\nThis means that it is not open for extension.",
                        ReflectionUtils.simpleName(schedulable)
                );
            } else {
                throw e;
            }
        }
        List dates = (List) calendar.invoke(ITEMS_METHOD_NAME);
        assertEquals(3, dates.size(),
                "Expected `Calendar.items` to return 3 items after `addSchedulable` was called 3 times");

        new ClassProxy(EVENT_CLASS_NAME).ensureImplements(_Schedulable);
        new ClassProxy(REMINDER_CLASS_NAME).ensureImplements(_Schedulable);

        Class<?> todoClass = new ClassProxy(TODO_CLASS_NAME).getDelegate();
        if (_Schedulable.getDelegate().isAssignableFrom(todoClass)) {
            failFormat("`Todo` should *not* implement `%s`", _Schedulable.getDelegate().getSimpleName());
        }

        List<String> todoMethodNames = Arrays.stream(todoClass.getDeclaredMethods())
                .map(Method::getName).collect(Collectors.toList());
        if (todoMethodNames.contains(getLocalDateTimeMethod.getName())) {
            failFormat("Expected `Todo` to _not_ define `%s`", getLocalDateTimeMethod.getName());
        }
    }

}
