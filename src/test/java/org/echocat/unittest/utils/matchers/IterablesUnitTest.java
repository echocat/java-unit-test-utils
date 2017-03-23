package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.Iterables.*;
import static org.echocat.unittest.utils.matchers.Iterables.endsWith;
import static org.echocat.unittest.utils.matchers.Iterables.startsWith;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IterablesUnitTest {

    @Test
    public void factoryMethodStartsWith() throws Exception {
        final Matcher<List<String>> instance = startsWith("a", "b", "c");

        assertThat(instance, instanceOf(Iterables.class));
        assertThat(((Iterables<?, ?>) instance).comparator(), sameInstance(Iterables.startsWithComparator()));
        assertThat(((Iterables<?, ?>) instance).comparatorDescription(), equalTo("starts with"));
        assertThat(((Iterables<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void factoryMethodEndsWith() throws Exception {
        final Matcher<List<String>> instance = endsWith("a", "b", "c");

        assertThat(instance, instanceOf(Iterables.class));
        assertThat(((Iterables<?, ?>) instance).comparator(), sameInstance(Iterables.endsWithComparator()));
        assertThat(((Iterables<?, ?>) instance).comparatorDescription(), equalTo("ends with"));
        assertThat(((Iterables<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void factoryMethodContains() throws Exception {
        final Matcher<List<String>> instance = contains("a", "b", "c");

        assertThat(instance, instanceOf(Iterables.class));
        assertThat(((Iterables<?, ?>) instance).comparator(), sameInstance(Iterables.containsComparator()));
        assertThat(((Iterables<?, ?>) instance).comparatorDescription(), equalTo("contains"));
        assertThat(((Iterables<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void constructor() throws Exception {
        final Iterables<String, List<String>> instance = new Iterables<>("test", Iterables.startsWithComparator(), iterableOf("a", "b", "c"));

        assertThat(instance.comparator(), sameInstance(Iterables.startsWithComparator()));
        assertThat(instance.comparatorDescription(), equalTo("test"));
        assertThat(instance.expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void startsWithComparator() throws Exception {
        final Comparator<Integer> instance = Iterables.startsWithComparator();

        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 4, 5)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 3, 4)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf()), equalTo(true));
    }

    @Test
    public void endsWithComparator() throws Exception {
        final Comparator<Integer> instance = Iterables.endsWithComparator();

        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 4, 5)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 4, 6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 3, 4)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(-1, 0, 1)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf()), equalTo(true));
    }

    @Test
    public void containsComparator() throws Exception {
        final Comparator<Integer> instance = Iterables.containsComparator();

        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 4, 5)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 3, 4)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 3, 6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(6)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(-1, 0, 1)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf()), equalTo(true));
    }

    @Test
    public void matches() throws Exception {
        final Matcher<Iterable<Integer>> instance = givenStartsWith123Instance();

        assertThat(instance.matches(iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.matches(iterableOf(1, 2, 3)), equalTo(false));
        assertThat(instance.matches("666"), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final Matcher<Iterable<Integer>> instance = givenStartsWith123Instance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("starts with [<0>, <1>, <2>]"));
    }

    @Test
    public void skipOneSucceedsWithMoreThanOneLeftEntries() throws Exception {
        final Iterator<Integer> actual = givenIteratorWith2LeftEntries();

        skipOne(actual);

        assertThat(actual.hasNext(), equalTo(true));
        assertThat(actual.next(), equalTo(1));
        assertThat(actual.hasNext(), equalTo(false));
    }

    @Test
    public void skipOneSucceedsWithOneLeftEntry() throws Exception {
        final Iterator<Integer> actual = givenIteratorWithOneLeftEntry();

        skipOne(actual);

        assertThat(actual.hasNext(), equalTo(false));
    }

    @Test
    public void skipOneFailsWithNoLeftEntry() throws Exception {
        final Iterator<Integer> actual = givenEmptyIterator();

        assertThat(() -> skipOne(actual), throwsException(IllegalStateException.class));
    }

    @Nonnull
    protected static Iterator<Integer> givenIteratorWith2LeftEntries() {
        return iterableOf(0, 1).iterator();
    }

    @Nonnull
    protected static Iterator<Integer> givenIteratorWithOneLeftEntry() {
        return iterableOf(0).iterator();
    }

    @Nonnull
    protected static Iterator<Integer> givenEmptyIterator() {
        return emptyIterator();
    }

    @Nonnull
    protected static Iterables<Integer, Iterable<Integer>> givenStartsWith123Instance() {
        return new Iterables<>("starts with", Iterables.startsWithComparator(), iterableOf(0, 1, 2));
    }

    @SafeVarargs
    @Nonnull
    protected static <T> Iterable<T> iterableOf(@Nonnull T... values) {
        return asList(values);
    }
}
