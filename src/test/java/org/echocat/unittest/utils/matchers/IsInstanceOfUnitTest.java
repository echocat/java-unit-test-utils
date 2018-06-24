package org.echocat.unittest.utils.matchers;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

import static org.echocat.unittest.utils.matchers.IsInstanceOf.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IsInstanceOfUnitTest {

    @Test
    void factoryMethodIsInstanceOf() throws Exception {
        final Matcher<Object> matcher = isInstanceOf(Array.class);

        assertThat(matcher, CoreMatchers.instanceOf(IsInstanceOf.class));
        assertThat(((IsInstanceOf<?>) matcher).expectedType(), equalTo(Array.class));
    }

    @Test
    void factoryMethodInstanceOf() throws Exception {
        final Matcher<Object> matcher = instanceOf(Array.class);

        assertThat(matcher, CoreMatchers.instanceOf(IsInstanceOf.class));
        assertThat(((IsInstanceOf<?>) matcher).expectedType(), equalTo(Array.class));
    }

    @Test
    void factoryMethodAny() throws Exception {
        final Matcher<Array> matcher = any(Array.class);

        assertThat(matcher, CoreMatchers.instanceOf(IsInstanceOf.class));
        assertThat(((IsInstanceOf<?>) (Object) matcher).expectedType(), equalTo(Array.class));
    }



}
