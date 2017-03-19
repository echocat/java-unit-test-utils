package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.IsEmpty.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class IsEmptyUnitTest {

    @Test
    public void factoryMethodIsEmpty() throws Exception {
        final Matcher<Object> instance = isEmpty();

        assertThat(instance, instanceOf(IsEmpty.class));
    }

    @Test
    public void factoryMethodEmpty() throws Exception {
        final Matcher<Object> instance = empty();

        assertThat(instance, instanceOf(IsEmpty.class));
    }

    @Test
    public void factoryMethodHasNoItems() throws Exception {
        final Matcher<Object> instance = hasNoItems();

        assertThat(instance, instanceOf(IsEmpty.class));
    }

    @Test
    public void matches() throws Exception {
        final IsEmpty instance = givenInstance();

        assertThat(instance.matches(""), equalTo(true));
        assertThat(instance.matches("abc"), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final IsEmpty instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("is <empty>"));
    }

    @Test
    public void describeMismatch() throws Exception {
        final Description description = givenDescription();
        final IsEmpty instance = givenInstance();

        instance.describeMismatch("item", description);

        assertThat(description.toString(), equalTo("has items: \"item\""));
    }

    @Nonnull
    protected static IsEmpty givenInstance() {
        return new IsEmpty();
    }

}
