package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.echocat.unittest.utils.matchers.StringMatchers.endsWith;
import static org.echocat.unittest.utils.matchers.StringMatchers.startsWith;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringMatchersUnitTest {

    @Test
    public void factoryMethodStartsWith() throws Exception {
        final Matcher<String> instance = startsWith("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.startsWithComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("starts with"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEndsWith() throws Exception {
        final Matcher<String> instance = endsWith("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.endsWithComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("ends with"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodContains() throws Exception {
        final Matcher<String> instance = StringMatchers.contains("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.containsComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("contains"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodMatches() throws Exception {
        final Matcher<String> instance = StringMatchers.matches("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.matchesComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("matches regular expression"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEqualsIgnoreCase() throws Exception {
        //noinspection LiteralAsArgToStringEquals
        final Matcher<String> instance = StringMatchers.equalsIgnoreCase("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.equalsIgnoreCaseComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodIsEqualsIgnoreCase() throws Exception {
        final Matcher<String> instance = StringMatchers.isEqualIgnoreCase("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.equalsIgnoreCaseComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodIsEqualToIgnoreCase() throws Exception {
        final Matcher<String> instance = StringMatchers.isEqualToIgnoreCase("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.equalsIgnoreCaseComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEqualsToIgnoreCase() throws Exception {
        final Matcher<String> instance = StringMatchers.equalsToIgnoreCase("666");

        assertThat(instance, instanceOf(StringBasedMatcher.class));
        assertThat(((StringBasedMatcher<?>) instance).comparator(), sameInstance(StringBasedMatcher.equalsIgnoreCaseComparator()));
        assertThat(((StringBasedMatcher<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((StringBasedMatcher<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void constructor() {
        new StringMatchers();
    }

}
