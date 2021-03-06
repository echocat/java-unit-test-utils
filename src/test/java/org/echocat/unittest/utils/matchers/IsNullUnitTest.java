package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.echocat.unittest.utils.matchers.IsNull.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsNullUnitTest {

    @Test
    public void factoryMethodNullValue() throws Exception {
        final Matcher<Object> instance = nullValue();

        assertThat(instance, instanceOf(IsNull.class));
    }

    @Test
    public void factoryMethodIsNull() throws Exception {
        final Matcher<Object> instance = isNull();

        assertThat(instance, instanceOf(IsNull.class));
    }

    @Test
    public void factoryMethodIsNullValue() throws Exception {
        final Matcher<Object> instance = isNullValue();

        assertThat(instance, instanceOf(IsNull.class));
    }

    @Test
    public void factoryMethodNotNullValue() throws Exception {
        final Matcher<Object> instance = notNullValue();

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>)instance).enclosed(), instanceOf(IsNull.class));
    }

    @Test
    public void factoryMethodIsNotNull() throws Exception {
        final Matcher<Object> instance = isNotNull();

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>)instance).enclosed(), instanceOf(IsNull.class));
    }

    @Test
    public void factoryMethodIsNotNullValue() throws Exception {
        final Matcher<Object> instance = isNotNullValue();

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>)instance).enclosed(), instanceOf(IsNull.class));
    }

}
