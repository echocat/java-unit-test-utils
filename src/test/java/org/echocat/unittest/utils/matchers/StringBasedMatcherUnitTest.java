package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.StringBasedMatcher.*;
import static org.echocat.unittest.utils.matchers.StringMatchers.endsWith;
import static org.echocat.unittest.utils.matchers.StringMatchers.startsWith;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringBasedMatcherUnitTest {

    @Test
    public void startsWithComparator() throws Exception {
        final Comparator instance = StringBasedMatcher.startsWithComparator();

        assertThat(instance.check("666 hello world", "666"), equalTo(true));
        assertThat(instance.check("hello 666 world", "666"), equalTo(false));
        assertThat(instance.check("hello world 666", "666"), equalTo(false));
    }

    @Test
    public void endsWithComparator() throws Exception {
        final Comparator instance = StringBasedMatcher.endsWithComparator();

        assertThat(instance.check("666 hello world", "666"), equalTo(false));
        assertThat(instance.check("hello 666 world", "666"), equalTo(false));
        assertThat(instance.check("hello world 666", "666"), equalTo(true));
    }

    @Test
    public void containsComparator() throws Exception {
        final Comparator instance = StringBasedMatcher.containsComparator();

        assertThat(instance.check("66 hello world", "666"), equalTo(false));
        assertThat(instance.check("666 hello world", "666"), equalTo(true));
        assertThat(instance.check("hello 666 world", "666"), equalTo(true));
        assertThat(instance.check("hello world 666", "666"), equalTo(true));
    }

    @Test
    public void matchesComparator() throws Exception {
        final Comparator instance = StringBasedMatcher.matchesComparator();

        assertThat(instance.check("66 hello world", ".*666.*"), equalTo(false));
        assertThat(instance.check("666 hello world", ".*666.*"), equalTo(true));
        assertThat(instance.check("hello 666 world", ".*666.*"), equalTo(true));
        assertThat(instance.check("hello world 666", ".*666.*"), equalTo(true));
    }

    @Test
    public void equalsIgnoreCaseComparator() throws Exception {
        final Comparator instance = StringBasedMatcher.equalsIgnoreCaseComparator();

        assertThat(instance.check("hello world", "hello world"), equalTo(true));
        assertThat(instance.check("hElLo WoRlD", "hello world"), equalTo(true));
        assertThat(instance.check("hElLo WoRlDs", "hello world"), equalTo(false));
    }

    @Test
    public void constructor() throws Exception {
        final StringBasedMatcher<String> instance = new StringBasedMatcher<>("test", StringBasedMatcher.startsWithComparator(), "666");

        assertThat(instance.comparator(), sameInstance(StringBasedMatcher.startsWithComparator()));
        assertThat(instance.comparatorDescription(), equalTo("test"));
        assertThat(instance.expected(), equalTo("666"));
    }

    @Test
    public void matches() throws Exception {
        final Matcher<String> instance = givenStartsWith666Instance();

        assertThat(instance.matches("666 hello"), equalTo(true));
        assertThat(instance.matches("hello 666"), equalTo(false));
        assertThat(instance.matches(666), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final Matcher<String> instance = givenStartsWith666Instance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("starts with \"666\""));
    }

    @Nonnull
    protected static Matcher<String> givenStartsWith666Instance() {
        return new StringBasedMatcher<>("starts with", StringBasedMatcher.startsWithComparator(), "666");
    }

}
