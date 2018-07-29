package org.echocat.unittest.utils.nio;

import org.junit.Test;

import java.util.Optional;

import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isAbsent;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.whereContentMatches;
import static org.echocat.unittest.utils.nio.Event.eventOf;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class InterceptorUnitTest {

    private static final EventType TYPE = EventType.eventTypeOf(ExampleClass.class, "foo", String.class);

    @Test
    public void before_is_always_empty() throws Exception {
        final Event event = eventOf(TYPE, "foo");
        final Interceptor instance = (e, r) -> Optional.of(r);

        final Optional<Object> actual = instance.before(event);

        assertThat(actual, isAbsent());

        assertThat(instance.after(event, "abc"), whereContentMatches(isEqualTo("abc")));
    }

    private static class ExampleClass {

        private String foo(String input) {
            return input;
        }

    }

}