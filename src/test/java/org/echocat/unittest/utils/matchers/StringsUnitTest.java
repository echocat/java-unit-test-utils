package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.Strings.*;
import static org.echocat.unittest.utils.matchers.Strings.endsWith;
import static org.echocat.unittest.utils.matchers.Strings.startsWith;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringsUnitTest {

    @Test
    public void factoryMethodStartsWith() throws Exception {
        final Matcher<String> instance = startsWith("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(startsWithComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("starts with"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEndsWith() throws Exception {
        final Matcher<String> instance = endsWith("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(endsWithComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("ends with"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodContains() throws Exception {
        final Matcher<String> instance = contains("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(containsComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("contains"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodMatches() throws Exception {
        final Matcher<String> instance = Strings.matches("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(matchesComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("matches regular expression"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEqualsIgnoreCase() throws Exception {
        final Matcher<String> instance = equalsIgnoreCase("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(equalsIgnoreCaseComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodIsEqualsIgnoreCase() throws Exception {
        final Matcher<String> instance = isEqualIgnoreCase("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(equalsIgnoreCaseComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodIsEqualToIgnoreCase() throws Exception {
        final Matcher<String> instance = isEqualToIgnoreCase("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(equalsIgnoreCaseComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodEqualsToIgnoreCase() throws Exception {
        final Matcher<String> instance = equalsToIgnoreCase("666");

        assertThat(instance, instanceOf(Strings.class));
        assertThat(((Strings<?>) instance).comparator(), sameInstance(equalsIgnoreCaseComparator()));
        assertThat(((Strings<?>) instance).comparatorDescription(), equalTo("equals ignore case"));
        assertThat(((Strings<?>) instance).expected(), equalTo("666"));
    }

    @Test
    public void factoryMethodStartsWithComparator() throws Exception {
        final Comparator instance = startsWithComparator();

        assertThat(instance.check("666 hello world", "666"), equalTo(true));
        assertThat(instance.check("hello 666 world", "666"), equalTo(false));
        assertThat(instance.check("hello world 666", "666"), equalTo(false));
    }

    @Test
    public void factoryMethodEndsWithComparator() throws Exception {
        final Comparator instance = endsWithComparator();

        assertThat(instance.check("666 hello world", "666"), equalTo(false));
        assertThat(instance.check("hello 666 world", "666"), equalTo(false));
        assertThat(instance.check("hello world 666", "666"), equalTo(true));
    }

    @Test
    public void factoryMethodContainsComparator() throws Exception {
        final Comparator instance = containsComparator();

        assertThat(instance.check("66 hello world", "666"), equalTo(false));
        assertThat(instance.check("666 hello world", "666"), equalTo(true));
        assertThat(instance.check("hello 666 world", "666"), equalTo(true));
        assertThat(instance.check("hello world 666", "666"), equalTo(true));
    }

    @Test
    public void factoryMethodMatchesComparator() throws Exception {
        final Comparator instance = matchesComparator();

        assertThat(instance.check("66 hello world", ".*666.*"), equalTo(false));
        assertThat(instance.check("666 hello world", ".*666.*"), equalTo(true));
        assertThat(instance.check("hello 666 world", ".*666.*"), equalTo(true));
        assertThat(instance.check("hello world 666", ".*666.*"), equalTo(true));
    }

    @Test
    public void factoryMethodEqualsIgnoreCaseComparator() throws Exception {
        final Comparator instance = equalsIgnoreCaseComparator();

        assertThat(instance.check("hello world", "hello world"), equalTo(true));
        assertThat(instance.check("hElLo WoRlD", "hello world"), equalTo(true));
        assertThat(instance.check("hElLo WoRlDs", "hello world"), equalTo(false));
    }

    @Test
    public void constructor() throws Exception {
        final Strings<String> instance = new Strings<>("test", startsWithComparator(), "666");

        assertThat(instance.comparator(), sameInstance(startsWithComparator()));
        assertThat(instance.comparatorDescription(), equalTo("test"));
        assertThat(instance.expected(), equalTo("666"));
    }

    @Test
    public void matches() throws Exception {
        final Matcher<String> instance = givenStartsWith666Instance();

        assertThat(instance.matches("666 hello"), equalTo(true));
        assertThat(instance.matches("hello 666"), equalTo(false));
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
        return new Strings<>("starts with", startsWithComparator(), "666");
    }

}
