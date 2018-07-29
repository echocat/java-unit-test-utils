package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.Event.Default;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsInstanceOf.isInstanceOf;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSameAs;
import static org.echocat.unittest.utils.matchers.IsSameAs.sameAs;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.nio.Event.eventOf;
import static org.junit.Assert.assertThat;

public class EventUnitTest {

    private static final EventType TYPE = EventType.eventTypeOf(ExampleClass.class, "foo", String.class, Integer.TYPE);

    @Test
    void eventOf_creates_event_as_expected() {
        final Event actual = eventOf(TYPE, "a", 2);

        assertThat(actual, isInstanceOf(Default.class));
        assertThat(actual.type(), isSameAs(TYPE));
        assertThat(actual.arguments(), isEqualTo(asList("a", 2)));
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    static class DefaultUnitTest {

        @Test
        void constructor_works_as_expected() {
            final Collection<Object> arguments = asList("a", 2);
            final Default actual = new Default(TYPE, arguments);

            assertThat(actual.type(), isSameAs(TYPE));
            assertThat(actual.arguments(), isEqualTo(arguments));
        }

        @Test
        void constructor_fails_on_too_few_arguments() {
            assertThat(() -> new Default(TYPE, asList("a"))
                , throwsException(IllegalArgumentException.class, "Expected 2 arguments.+but got 1\\."));
        }

        @Test
        void constructor_fails_on_too_much_arguments() {
            assertThat(() -> new Default(TYPE, asList("a", 2, 3L))
                , throwsException(IllegalArgumentException.class, "Expected 2 arguments.+but got 3\\."));
        }

        @Test
        void constructor_fails_on_mismatching_arguments() {
            assertThat(() -> new Default(TYPE, asList("a", 2L))
                , throwsException(IllegalArgumentException.class, "Expected argument of type class java.lang.Integer at index #1 .+ but got java.lang.Long: 2"));
        }

        @Test
        void constructor_fails_on_primitive_argument_type() {
            assertThat(() -> new Default(new TypeWithPrimitive(), asList("a", 2))
                , throwsException(IllegalArgumentException.class, ".+ specified argument of type int at index #1 which is a primitive. .+"));
        }

        @Test
        void equals_and_hashCode_matches() {
            final Default a = new Default(TYPE, asList("a", 2));
            final Default b = new Default(TYPE, asList("a", 2));

            assertThat(a, isNot(sameAs(b)));
            assertThat(a.equals(b), isEqualTo(true));
            assertThat(a.hashCode(), isEqualTo(b.hashCode()));
        }

        @Test
        void equals_and_hashCode_mismatches() {
            final Default a = new Default(TYPE, asList("a", 2));
            final Default b = new Default(TYPE, asList("b", 2));

            assertThat(a, isNot(sameAs(b)));
            assertThat(a.equals(b), isEqualTo(false));
            assertThat(a.hashCode(), isNot(equalTo(b.hashCode())));
        }

        @Test
        void toString_works_as_expected() {
            final Default instance = new Default(TYPE, asList("a", 2));

            final String actual = instance.toString();

            assertThat(actual, isEqualTo("Void: ExampleClass#foo(" +
                "\n\tString: a," +
                "\n\tInteger: 2" +
                "\n)"));
        }

    }

    private static class ExampleClass {

        private void foo(String aString, int anInteger) {
        }

    }

    @SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
    private static class TypeWithPrimitive implements EventType {

        @Nonnull
        @Override
        public Class<?> sourceType() {
            throw new UnsupportedOperationException();
        }

        @Nonnull
        @Override
        public String name() {
            throw new UnsupportedOperationException();
        }

        @Nonnull
        @Override
        public List<Class<?>> argumentTypes() {
            return asList(String.class, int.class);
        }

        @Nonnull
        @Override
        public Class<?> returnType() {
            throw new UnsupportedOperationException();
        }

        @Nonnull
        @Override
        public Set<Class<? extends Throwable>> allowedThrowableTypes() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int compareTo(@Nonnull EventType o) {
            throw new UnsupportedOperationException();
        }
    }

}