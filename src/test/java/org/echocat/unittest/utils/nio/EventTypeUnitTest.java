package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.EventType.Default;
import org.echocat.unittest.utils.nio.Relation.Object;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.echocat.unittest.utils.matchers.HasSize.hasLengthOf;
import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsInstanceOf.isInstanceOf;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.echocat.unittest.utils.matchers.IsSameAs.sameAs;
import static org.echocat.unittest.utils.matchers.IterableMatchers.contains;
import static org.echocat.unittest.utils.nio.EventType.eventTypeOf;
import static org.junit.Assert.assertThat;

public class EventTypeUnitTest {

    @Test
    void eventTypeOf_creates_as_expected() {
        final EventType actual = eventTypeOf(ExampleClass.class, "foo", String.class, int.class);

        assertThat(actual, isInstanceOf(Default.class));
        assertThat(actual.sourceType(), isEqualTo(ExampleClass.class));
        assertThat(actual.name(), isEqualTo("foo"));
        assertThat(actual.argumentTypes(), isEqualTo(asList(String.class, Integer.class)));
        assertThat(actual.returnType(), isEqualTo(List.class));
        assertThat(actual.allowedThrowableTypes(), hasLengthOf(2));
        assertThat(actual.allowedThrowableTypes(), contains(IOException.class));
        assertThat(actual.allowedThrowableTypes(), contains(SecurityException.class));
    }

    @SuppressWarnings("EqualsWithItself")
    static class DefaultUnitTest {

        @Test
        void constructor_works_as_expected() {
            final Default actual = new Default(
                ExampleClass.class,
                "foo",
                asList(String.class, Integer.class),
                List.class,
                asList(IOException.class, SecurityException.class)
            );

            assertThat(actual.sourceType(), isEqualTo(ExampleClass.class));
            assertThat(actual.name(), isEqualTo("foo"));
            assertThat(actual.argumentTypes(), isEqualTo(asList(String.class, Integer.class)));
            assertThat(actual.returnType(), isEqualTo(List.class));
            assertThat(actual.allowedThrowableTypes(), hasLengthOf(2));
            assertThat(actual.allowedThrowableTypes(), contains(IOException.class));
            assertThat(actual.allowedThrowableTypes(), contains(SecurityException.class));
        }

        @Test
        void constructor_normalizes_types() {
            final Default actual = new Default(
                ExampleClass.class,
                "foo",
                asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class),
                void.class,
                emptyList()
            );

            assertThat(actual.argumentTypes(), isEqualTo(asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class)));
            assertThat(actual.returnType(), isEqualTo(Void.class));
        }

        @Test
        void equals_and_hashCode_matches() {
            final Default a = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));
            final Default b = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));

            assertThat(a, isNot(sameAs(b)));
            assertThat(a.equals(b), isEqualTo(true));
            assertThat(a.hashCode(), isEqualTo(b.hashCode()));
        }

        @Test
        void equals_and_hashCode_mismatches() {
            final Default a = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));
            final Default b = new Default(ExampleClass.class, "bar", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));

            assertThat(a, isNot(sameAs(b)));
            assertThat(a.equals(b), isEqualTo(false));
            assertThat(a.hashCode(), isNot(equalTo(b.hashCode())));
        }

        @Test
        void compareTo_on_sourceType_works_as_expected() {
            final Default a = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));
            final Default b = new Default(Object.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));

            assertThat(a.compareTo(a), isEqualTo(0));
            assertThat(a.compareTo(b), isNot(equalTo(0)));
        }

        @Test
        void compareTo_on_name_works_as_expected() {
            final Default a = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));
            final Default b = new Default(ExampleClass.class, "bar", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));

            assertThat(a.compareTo(a), isEqualTo(0));
            assertThat(a.compareTo(b), isNot(equalTo(0)));
        }

        @Test
        void compareTo_on_parameters_works_as_expected() {
            final Default a = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));
            final Default b = new Default(ExampleClass.class, "foo", asList(String.class, Long.class), List.class, asList(IOException.class, SecurityException.class));

            assertThat(a.compareTo(a), isEqualTo(0));
            assertThat(a.compareTo(b), isNot(equalTo(0)));
        }

        @Test
        void toString_works_as_expected() {
            final Default instance = new Default(ExampleClass.class, "foo", asList(String.class, Integer.class), List.class, asList(IOException.class, SecurityException.class));

            final String actual = instance.toString();

            assertThat(actual, isEqualTo("List: ExampleClass#foo(String, Integer)"));
        }



    }

    private static class ExampleClass {

        private List<?> foo(String aString, int anInteger) throws IOException, SecurityException {
            throw new UnsupportedOperationException();
        }

    }

}