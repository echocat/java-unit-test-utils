package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.IsEqualTo.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsEqualToUnitTest {

    @Test
    void factoryMethodEqualTo() throws Exception {
        final Matcher<Object> matcher = IsEqualTo.equalTo(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    void factoryMethodIsEqualTo() throws Exception {
        final Matcher<Object> matcher = isEqualTo(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    void factoryMethodIs() throws Exception {
        final Matcher<Object> matcher = is(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    void factoryMethodIsTrue() throws Exception {
        final Matcher<Boolean> matcher = isTrue();

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(true));
    }

    @Test
    void factoryMethodIsFalse() throws Exception {
        final Matcher<Boolean> matcher = isFalse();

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(false));
    }

    @Test
    void constructor() throws Exception {
        final Matcher<Object> matcher = new IsEqualTo<>(666L);

        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }


    @Test
    void describeTo() throws Exception {
        final Description description = givenDescription();
        final IsEqualTo<String> instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("is equal to \"Bert\""));
    }

    @Nonnull
    private static IsEqualTo<String> givenInstance() {
        return new IsEqualTo<>("Bert");
    }

}
