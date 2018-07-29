package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.echocat.unittest.utils.TestUtils.givenArrayWithLength3;
import static org.echocat.unittest.utils.TestUtils.givenArrayWithLength4;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.HasSameSizeAs.hasSameLengthAs;
import static org.echocat.unittest.utils.matchers.HasSameSizeAs.hasSameSizeAs;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class HasSameSizeAsUnitTest {

    @Test
    void factoryMethodHasSameSizeAs() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final Matcher<Object> matcher = hasSameSizeAs(compareTo);

        assertThat(matcher, instanceOf(HasSameSizeAs.class));
        assertThat(((HasSameSizeAs<?>) matcher).compareTo(), sameInstance(compareTo));
    }

    @Test
    void factoryMethodHasSameLengthAs() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final Matcher<Object> matcher = hasSameLengthAs(compareTo);

        assertThat(matcher, instanceOf(HasSameSizeAs.class));
        assertThat(((HasSameSizeAs<?>) matcher).compareTo(), sameInstance(compareTo));
    }

    @Test
    void constructor() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final HasSameSizeAs<Object> matcher = new HasSameSizeAs<>(compareTo);

        assertThat(((HasSameSizeAs<?>) matcher).compareTo(), sameInstance(compareTo));
    }

    @Test
    void matches() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final HasSameSizeAs<Object> instance = givenInstance(compareTo);

        assertThat(instance.matches("0123"), equalTo(true));
        assertThat(instance.matches("012"), equalTo(false));

        assertThat(instance.matches(new byte[4]), equalTo(true));
        assertThat(instance.matches(new byte[3]), equalTo(false));
    }

    @Test
    void describeTo() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final HasSameSizeAs<Object> instance = givenInstance(compareTo);
        final Description description = givenDescription();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("has size of <4L> - same as [<0>, <1>, <2>, <3>]"));
    }

    @Test
    void describeMismatch() throws Exception {
        final Object[] compareTo = givenArrayWithLength4();
        final Object[] toTest = givenArrayWithLength3();
        final HasSameSizeAs<Object> instance = givenInstance(compareTo);
        final Description description = givenDescription();

        instance.matches(toTest);
        instance.describeMismatch(toTest, description);

        assertThat(description.toString(), equalTo("was <3L> with content [<0>, <1>, <2>]"));
    }

    @Nonnull
    private static HasSameSizeAs<Object> givenInstance(@Nullable Object compareTo) {
        return new HasSameSizeAs<>(compareTo);
    }

}
