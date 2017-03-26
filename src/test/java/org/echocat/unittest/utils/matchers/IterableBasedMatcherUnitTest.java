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
import static org.echocat.unittest.utils.matchers.IterableBasedMatcher.Comparator;
import static org.echocat.unittest.utils.matchers.IterableBasedMatcher.skipOne;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class IterableBasedMatcherUnitTest {
    @Test
    public void constructor() throws Exception {
        final IterableBasedMatcher<String, List<String>> instance = new IterableBasedMatcher<>("test", IterableBasedMatcher.startsWithComparator(), iterableOf("a", "b", "c"));

        assertThat(instance.comparator(), sameInstance(IterableBasedMatcher.startsWithComparator()));
        assertThat(instance.comparatorDescription(), equalTo("test"));
        assertThat(instance.expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void startsWithComparator() throws Exception {
        final Comparator<Integer> instance = IterableBasedMatcher.startsWithComparator();

        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(3, 4, 5)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf(2, 3, 4)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2), iterableOf(0, 1, 2)), equalTo(true));
        assertThat(instance.check(iterableOf(0, 1), iterableOf(0, 1, 2)), equalTo(false));
        assertThat(instance.check(iterableOf(0, 1, 2, 3, 4, 5), iterableOf()), equalTo(true));
    }

    @Test
    public void endsWithComparator() throws Exception {
        final Comparator<Integer> instance = IterableBasedMatcher.endsWithComparator();

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
        final Comparator<Integer> instance = IterableBasedMatcher.containsComparator();

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
    protected static IterableBasedMatcher<Integer, Iterable<Integer>> givenStartsWith123Instance() {
        return new IterableBasedMatcher<>("starts with", IterableBasedMatcher.startsWithComparator(), iterableOf(0, 1, 2));
    }

    @SafeVarargs
    @Nonnull
    protected static <T> Iterable<T> iterableOf(@Nonnull T... values) {
        return asList(values);
    }
}
