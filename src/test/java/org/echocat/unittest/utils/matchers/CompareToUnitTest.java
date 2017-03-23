package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.CompareTo.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class CompareToUnitTest {

    @Test
    public void factoryMethodIsGreaterThan() throws Exception {
        final Matcher<Integer> instance = isGreaterThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.greaterThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsGreaterThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = isGreaterThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.greaterThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsLessThan() throws Exception {
        final Matcher<Integer> instance = isLessThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.lessThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsLessThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = isLessThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.lessThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodGreaterThan() throws Exception {
        final Matcher<Integer> instance = greaterThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.greaterThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodGreaterThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = greaterThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.greaterThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodLessThan() throws Exception {
        final Matcher<Integer> instance = lessThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.lessThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodLessThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = lessThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(CompareTo.lessThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void greaterThanComparator() throws Exception {
        final Comparator<Integer> instance = CompareTo.greaterThanComparator();

        assertThat(instance.check(2, 1), equalTo(true));
        assertThat(instance.check(1, 2), equalTo(false));
        assertThat(instance.check(1, 1), equalTo(false));
    }

    @Test
    public void greaterThanOrEqualToComparator() throws Exception {
        final Comparator<Integer> instance = CompareTo.greaterThanOrEqualToComparator();

        assertThat(instance.check(2, 1), equalTo(true));
        assertThat(instance.check(1, 2), equalTo(false));
        assertThat(instance.check(1, 1), equalTo(true));
    }

    @Test
    public void lessThanComparator() throws Exception {
        final Comparator<Integer> instance = CompareTo.lessThanComparator();

        assertThat(instance.check(2, 1), equalTo(false));
        assertThat(instance.check(1, 2), equalTo(true));
        assertThat(instance.check(1, 1), equalTo(false));
    }

    @Test
    public void lessThanOrEqualToComparator() throws Exception {
        final Comparator<Integer> instance = CompareTo.lessThanOrEqualToComparator();

        assertThat(instance.check(2, 1), equalTo(false));
        assertThat(instance.check(1, 2), equalTo(true));
        assertThat(instance.check(1, 1), equalTo(true));
    }

    @Test
    public void matches() throws Exception {
        final CompareTo<Integer> instance = givenEqualToOneInstance();

        assertThat(instance.matches(1), equalTo(true));
        assertThat(instance.matches(2), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final CompareTo<Integer> instance = givenEqualToOneInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("equals to <1>"));
    }

    @Nonnull
    protected static CompareTo<Integer> givenEqualToOneInstance() {
        return new CompareTo<>(1, "equals to", ((actual, expected) -> actual.compareTo(expected) == 0));
    }

}
