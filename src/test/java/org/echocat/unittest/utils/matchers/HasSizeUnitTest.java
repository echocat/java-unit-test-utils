package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import static org.echocat.unittest.utils.TestUtils.givenArrayWithLength3;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.TestUtils.givenEmptyArray;
import static org.echocat.unittest.utils.matchers.HasSize.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class HasSizeUnitTest {

    @Test
    public void factoryMethodHasSize() throws Exception {
        final Matcher<Object> matcher = hasSize(666);

        assertThat(matcher, instanceOf(HasSize.class));
        assertThat(((HasSize<?>) matcher).expectedSize(), equalTo(666L));
    }

    @Test
    public void factoryMethodHasSizeOf() throws Exception {
        final Matcher<Object> matcher = hasSizeOf(666);

        assertThat(matcher, instanceOf(HasSize.class));
        assertThat(((HasSize<?>) matcher).expectedSize(), equalTo(666L));
    }

    @Test
    public void factoryMethodHasLength() throws Exception {
        final Matcher<Object> matcher = hasLength(666);

        assertThat(matcher, instanceOf(HasSize.class));
        assertThat(((HasSize<?>) matcher).expectedSize(), equalTo(666L));
    }

    @Test
    public void factoryMethodHasLengthOf() throws Exception {
        final Matcher<Object> matcher = hasLengthOf(666);

        assertThat(matcher, instanceOf(HasSize.class));
        assertThat(((HasSize<?>) matcher).expectedSize(), equalTo(666L));
    }

    @Test
    public void constructor() throws Exception {
        final Matcher<Object> matcher = new HasSize<>(666);

        assertThat(((HasSize<?>) matcher).expectedSize(), equalTo(666L));
    }

    @Test
    public void matches() throws Exception {
        final HasSize<Object> instance = givenInstance();

        assertThat(instance.matches("0123"), equalTo(true));
        assertThat(instance.matches("012"), equalTo(false));

        assertThat(instance.matches(new byte[4]), equalTo(true));
        assertThat(instance.matches(new byte[3]), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final HasSize<Object> instance = givenInstance();
        final Description description = givenDescription();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("has size of <4L>"));
    }

    @Test
    public void describeMismatch() throws Exception {
        final Object[] toTest = givenArrayWithLength3();
        final HasSize<Object> instance = givenInstance();
        final Description description = givenDescription();

        instance.matches(toTest);
        instance.describeMismatch(toTest, description);

        assertThat(description.toString(), equalTo("was <3L> with content [<0>, <1>, <2>]"));
    }

    @Nonnull
    protected static HasSize<Object> givenInstance() {
        return new HasSize<>(4);
    }

}
