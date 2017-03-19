package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.echocat.unittest.utils.matchers.IsEqualTo.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsEqualToUnitTest {

    @Test
    public void factoryMethodEqualTo() throws Exception {
        final Matcher<Object> matcher = IsEqualTo.equalTo(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodIsEqualTo() throws Exception {
        final Matcher<Object> matcher = isEqualTo(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodIs() throws Exception {
        final Matcher<Object> matcher = is(666L);

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodIsTrue() throws Exception {
        final Matcher<Boolean> matcher = isTrue();

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(true));
    }

    @Test
    public void factoryMethodIsFalse() throws Exception {
        final Matcher<Boolean> matcher = isFalse();

        assertThat(matcher, instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(false));
    }

    @Test
    public void constructor() throws Exception {
        final Matcher<Object> matcher = new IsEqualTo<>(666L);

        assertThat(((IsEqualTo<?>) matcher).expected(), equalTo(666L));
    }



}
