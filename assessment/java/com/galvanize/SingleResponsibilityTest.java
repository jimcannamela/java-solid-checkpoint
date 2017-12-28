package com.galvanize;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import com.galvanize.util.MethodBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.galvanize.util.ReflectionUtils.failFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Single Responsibility Principle")
public class SingleResponsibilityTest {

    public static final String TODO_CLASS_NAME = "com.galvanize.Todo";

    @BeforeAll
    public static void verifyInitialState() {
        try {
            new ClassProxy(TODO_CLASS_NAME);
        } catch (AssertionFailedError e) {
            fail(e.getMessage() + ". Did you move, delete or rename it by mistake?");
        }
    }

    @Test
    @DisplayName("Objects should have a single responsibility")
    public void aTest() {
        ClassProxy _Todo = new ClassProxy(TODO_CLASS_NAME);
        _Todo.ensureConstructorCount(1);

        Constructor constructor = _Todo.getDelegate().getConstructors()[0];
        Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length != 2) {
            fail("Expected constructor for `Todo` to take 2 parameters, but found " + parameterTypes.length);
        }

        Class ownerClass;
        boolean stringFirst = false;
        if (parameterTypes[0] == String.class) {
            stringFirst = true;
            ownerClass = parameterTypes[1];
            _Todo.ensureConstructor(String.class, ownerClass);
        } else {
            ownerClass = parameterTypes[0];
            _Todo.ensureConstructor(ownerClass, String.class);
        }

        ClassProxy _Owner = ClassProxy.of(ownerClass)
                .ensureConstructor(String.class, String.class, String.class, String.class);
        InstanceProxy owner = _Owner.newInstance("a", "b", "c", "d");

        InstanceProxy todo;
        if (stringFirst) {
            todo = _Todo.newInstance("This is something todo", owner);
        } else {
            todo = _Todo.newInstance(owner, "This is something todo");
        }

        Method getter = null;
        try {
            getter = new MethodBuilder(_Todo.getDelegate()).returns(ownerClass).build();
        } catch (AssertionFailedError e) {
            failFormat("Expected `Todo` to define a getter for `%s` but found none", ownerClass.getSimpleName());
        }
        _Todo.ensureMethod(getter);
        Object getterValue = todo.invoke(getter);
        assertEquals(
                owner.getDelegate(),
                getterValue,
                String.format(
                        "Expected `Todo.%s` to return the same instance that was passed in the constructor, but it did not",
                        getter.getName()
                )
        );
    }

}
