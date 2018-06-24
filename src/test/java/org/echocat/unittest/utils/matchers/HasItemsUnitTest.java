package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.HasItems.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HasItemsUnitTest {

    @Test
    void factoryMethodHasItems() throws Exception {
        final Matcher<Object> instance = hasItems();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    void factoryMethodHasElements() throws Exception {
        final Matcher<Object> instance = hasElements();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    void factoryMethodIsNotEmpty() throws Exception {
        final Matcher<Object> instance = isNotEmpty();

        assertThat(instance, instanceOf(HasItems.class));
    }

    @Test
    void matches() throws Exception {
        final HasItems instance = givenInstance();

        assertThat(instance.matches(""), equalTo(false));
        assertThat(instance.matches("abc"), equalTo(true));
    }

    @Test
    void describeTo() throws Exception {
        final Description description = givenDescription();
        final HasItems instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("has items"));
    }

    @Test
    void describeMismatch() throws Exception {
        final Description description = givenDescription();
        final HasItems instance = givenInstance();

        instance.describeMismatch("item", description);

        assertThat(description.toString(), equalTo("was <empty>"));
    }

    @Nonnull
    private static HasItems givenInstance() {
        return new HasItems();
    }

}
