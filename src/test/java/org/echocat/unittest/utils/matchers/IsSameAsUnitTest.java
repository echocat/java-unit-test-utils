package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.echocat.unittest.utils.matchers.IsSameAs.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsSameAsUnitTest {

    @Test
    public void factoryMethodIsSameAs() throws Exception {
        final Matcher<Object> matcher = isSameAs(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodIsSame() throws Exception {
        final Matcher<Object> matcher = isSame(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodIsSameInstance() throws Exception {
        final Matcher<Object> matcher = isSameInstance(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void factoryMethodSameInstance() throws Exception {
        final Matcher<Object> matcher = sameInstance(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
    }

    @Test
    public void constructor() throws Exception {
        final IsSameAs<Long> matcher = new IsSameAs<>(666L);

        assertThat(matcher.expected(), equalTo(666L));
    }

}
