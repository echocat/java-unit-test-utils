package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.IsOneOf.isAnyOf;
import static org.echocat.unittest.utils.matchers.IsOneOf.isOneOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsOneOfUnitTest {

    @Test
    void factoryMethodIsOneOf() throws Exception {
        final Matcher<Object> matcher = isOneOf(0L, 1L, 2L);

        assertThat(matcher, instanceOf(IsOneOf.class));
        assertThat(((IsOneOf<?>) matcher).expected(), equalTo(asList(0L, 1L, 2L)));
    }

    @Test
    void factoryMethodIsAnyOf() throws Exception {
        final Matcher<Object> matcher = isAnyOf(0L, 1L, 2L);

        assertThat(matcher, instanceOf(IsOneOf.class));
        assertThat(((IsOneOf<?>) matcher).expected(), equalTo(asList(0L, 1L, 2L)));
    }

    @Test
    void constructor() throws Exception {
        final IsOneOf<Long> matcher = new IsOneOf<>(asList(0L, 1L, 2L));

        assertThat(matcher.expected(), equalTo(asList(0L, 1L, 2L)));
    }

    @Test
    void matches() throws Exception {
        final IsOneOf<Long> instance = givenInstanceWith012();

        assertThat(instance.matches(0L), equalTo(true));
        assertThat(instance.matches(1L), equalTo(true));
        assertThat(instance.matches(2L), equalTo(true));
        assertThat(instance.matches(4L), equalTo(false));
        assertThat(instance.matches(""), equalTo(false));
        assertThat(instance.matches(0), equalTo(false));
        assertThat(instance.matches(1), equalTo(false));
        assertThat(instance.matches(2), equalTo(false));
    }

    @Test
    void describeTo() throws Exception {
        final Description description = givenDescription();
        final Matcher<Long> instance = givenInstanceWith012();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("is one of [<0L>, <1L>, <2L>]"));
    }

    @Nonnull
    private static IsOneOf<Long> givenInstanceWith012() {
        return new IsOneOf<>(asList(0L, 1L, 2L));
    }

}
