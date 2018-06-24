package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

import static org.echocat.unittest.utils.matchers.IsSameAs.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IsSameAsUnitTest {

    @Test
    void factoryMethodIsSameAs() throws Exception {
        final Matcher<Object> matcher = isSameAs(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("is same as"));
    }

    @Test
    void factoryMethodIsSame() throws Exception {
        final Matcher<Object> matcher = isSame(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("is same"));
    }

    @Test
    void factoryMethodIsSameInstance() throws Exception {
        final Matcher<Object> matcher = isSameInstance(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("is same instance"));
    }

    @Test
    void factoryMethodSameAs() throws Exception {
        final Matcher<Object> matcher = sameAs(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("same as"));
    }

    @Test
    void factoryMethodSame() throws Exception {
        final Matcher<Object> matcher = same(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("same"));
    }

    @Test
    void factoryMethodSameInstance() throws Exception {
        final Matcher<Object> matcher = sameInstance(666L);

        assertThat(matcher, instanceOf(IsSameAs.class));
        assertThat(((IsSameAs<?>) matcher).expected(), equalTo(666L));
        assertThat(((IsSameAs<?>) matcher).matcherName(), equalTo("same instance"));
    }

    @Test
    void constructor() throws Exception {
        final IsSameAs<Long> matcher = new IsSameAs<>(666L, "foo");

        assertThat(matcher.expected(), equalTo(666L));
        assertThat(matcher.matcherName(), equalTo("foo"));
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    @Test
    void matchesWorksAsExpected() throws Exception {
        final String a = new String("a");
        final String b = new String("a");
        final IsSameAs<String> matcher = new IsSameAs<>(a, "foo");

        assertThat(matcher.matches(a), equalTo(true));
        assertThat(matcher.matches(b), equalTo(false));
    }

    @Test
    void describeToWorksAsExpected() throws Exception {
        final IsSameAs<String> matcher = new IsSameAs<>("foo", "is same as");
        final Description description = new StringDescription();

        matcher.describeTo(description);

        assertThat(description.toString(), equalTo("is same as \"foo\""));
    }

}
