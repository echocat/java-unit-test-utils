package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.matchers.HasItems.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HasItemsUnitTest extends TestSupport {

    @Test
    public void factoryMethodHasItems() throws Exception {
        final Matcher<Object> instance = hasItems();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    public void factoryMethodHasElements() throws Exception {
        final Matcher<Object> instance = hasElements();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    public void factoryMethodIsNotEmpty() throws Exception {
        final Matcher<Object> instance = isNotEmpty();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    public void matches() throws Exception {
        final HasItems instance = givenInstance();

        assertThat(instance.matches(""), equalTo(false));
        assertThat(instance.matches("abc"), equalTo(true));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final HasItems instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("has items"));
    }

    @Test
    public void describeMismatch() throws Exception {
        final Description description = givenDescription();
        final HasItems instance = givenInstance();

        instance.describeMismatch("item", description);

        assertThat(description.toString(), equalTo("was <empty>"));
    }

    @Nonnull
    protected HasItems givenInstance() {
        return new HasItems();
    }

}
