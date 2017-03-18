package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.matchers.CompareTo.*;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class CompareToUnitTest extends TestSupport {

    @Test
    public void factoryMethodIsGreaterThan() throws Exception {
        final Matcher<Integer> instance = isGreaterThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(greaterThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsGreaterThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = isGreaterThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(greaterThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsLessThan() throws Exception {
        final Matcher<Integer> instance = isLessThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(lessThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodIsLessThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = isLessThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(lessThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodGreaterThan() throws Exception {
        final Matcher<Integer> instance = greaterThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(greaterThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodGreaterThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = greaterThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(greaterThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is greater than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodLessThan() throws Exception {
        final Matcher<Integer> instance = lessThan(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(lessThanComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodLessThanOrEqualTo() throws Exception {
        final Matcher<Integer> instance = lessThanOrEqualTo(666);

        assertThat(instance, instanceOf(CompareTo.class));
        assertThat(((CompareTo) instance).comparator(), sameInstance(lessThanOrEqualToComparator()));
        assertThat(((CompareTo) instance).comparatorDescription(), equalTo("is less than or equal to"));
        assertThat(((CompareTo) instance).expected(), equalTo(666));
    }

    @Test
    public void factoryMethodGreaterThanComparator() throws Exception {
        final Comparator<Integer> instance = greaterThanComparator();

        assertThat(instance.check(2, 1), equalTo(true));
        assertThat(instance.check(1, 2), equalTo(false));
        assertThat(instance.check(1, 1), equalTo(false));
    }

    @Test
    public void factoryMethodGreaterThanOrEqualToComparator() throws Exception {
        final Comparator<Integer> instance = greaterThanOrEqualToComparator();

        assertThat(instance.check(2, 1), equalTo(true));
        assertThat(instance.check(1, 2), equalTo(false));
        assertThat(instance.check(1, 1), equalTo(true));
    }

    @Test
    public void factoryMethodLessThanComparator() throws Exception {
        final Comparator<Integer> instance = lessThanComparator();

        assertThat(instance.check(2, 1), equalTo(false));
        assertThat(instance.check(1, 2), equalTo(true));
        assertThat(instance.check(1, 1), equalTo(false));
    }

    @Test
    public void factoryMethodLessThanOrEqualToComparator() throws Exception {
        final Comparator<Integer> instance = lessThanOrEqualToComparator();

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

    @Test
    public void typeOfDoesNotAcceptNull() throws Exception {
        assertThat(() -> typeOf(null), throwsException(NullPointerException.class, "The provided expected value is null."));
    }

    @Test
    public void typeOfDoesNotAcceptNonComparable() throws Exception {
        assertThat(() -> typeOf(new Object()), throwsException(IllegalArgumentException.class, "The provided expected value is not of type.*"));
    }

    @Test
    public void typeOfHappyPath() throws Exception {
        assertThat(typeOf(1), sameInstance(Integer.class));
    }

    @Nonnull
    protected CompareTo<Integer> givenEqualToOneInstance() {
        return new CompareTo<>(1, "equals to", ((actual, expected) -> actual.compareTo(expected) == 0));
    }

}
