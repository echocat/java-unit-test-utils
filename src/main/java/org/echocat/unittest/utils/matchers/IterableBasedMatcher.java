package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

import static java.util.Arrays.asList;

public class IterableBasedMatcher<V, T extends Iterable<V>> extends BaseMatcher<T> {

    @Nonnull
    protected static <V> Comparator<V> startsWithComparator() {
        //noinspection unchecked
        return (Comparator<V>) (Comparator<Object>) IterableBasedMatcher::startsWith;
    }

    @Nonnull
    protected static <V> Comparator<V> endsWithComparator() {
        //noinspection unchecked
        return (Comparator<V>) (Comparator<Object>) IterableBasedMatcher::endsWith;
    }

    @Nonnull
    protected static <V> Comparator<V> containsComparator() {
        //noinspection unchecked
        return (Comparator<V>) (Comparator<Object>) IterableBasedMatcher::contains;
    }

    @Nonnull
    private final Iterable<? extends V> expected;
    @Nonnull
    private final String comparatorDescription;
    @Nonnull
    private final Comparator<V> comparator;

    protected IterableBasedMatcher(@Nonnull String comparatorDescription, @Nonnull Comparator<V> comparator, @Nonnull Iterable<? extends V> expected) {
        this.comparatorDescription = comparatorDescription;
        this.comparator = comparator;
        this.expected = expected;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        if (item instanceof Iterable) {
            //noinspection unchecked
            return comparator.check((Iterable<V>) item, (Iterable<V>) expected);
        }
        return false;
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText(comparatorDescription).appendText(" [");
        boolean first = true;
        for (final V item : expected) {
            if (first) {
                first = false;
            } else {
                description.appendText(", ");
            }
            description.appendValue(item);
        }
        description.appendText("]");
    }

    @Nonnull
    protected Iterable<? extends V> expected() {
        return expected;
    }

    @Nonnull
    protected String comparatorDescription() {
        return comparatorDescription;
    }

    @Nonnull
    protected Comparator<V> comparator() {
        return comparator;
    }

    @FunctionalInterface
    public interface Comparator<V> {
        boolean check(@Nonnull Iterable<V> actual, @Nonnull Iterable<V> expected);
    }

    protected static boolean startsWith(@Nonnull Iterable<Object> actual, @Nonnull Iterable<Object> expected) {
        final Iterator<Object> ai = actual.iterator();
        final Iterator<Object> ei = expected.iterator();
        while (ai.hasNext() && ei.hasNext()) {
            final Object ao = ai.next();
            final Object eo = ei.next();
            if (!Objects.equals(ao, eo)) {
                return false;
            }
        }
        return !ei.hasNext();
    }

    protected static boolean endsWith(@Nonnull Iterable<Object> actual, @Nonnull Iterable<Object> expected) {
        final Iterator<Object> ai = actual.iterator();
        final Iterator<Object> fei = expected.iterator();
        if (!ai.hasNext() && fei.hasNext()) {
            return false;
        }
        if (!fei.hasNext()) {
            return true;
        }
        final Object feo = fei.next();
        while (ai.hasNext()) {
            final Object fao = ai.next();
            if (Objects.equals(fao, feo)) {
                final Iterator<Object> ei = expected.iterator();
                skipOne(ei);
                final boolean allMatches = continueMatchesOn(ai, ei);
                if (!ai.hasNext() && !ei.hasNext()) {
                    return allMatches;
                }
            }
        }
        return false;
    }

    protected static boolean contains(@Nonnull Iterable<Object> actual, @Nonnull Iterable<Object> expected) {
        final Iterator<Object> ai = actual.iterator();
        final Iterator<Object> fei = expected.iterator();
        if (!ai.hasNext() && fei.hasNext()) {
            return false;
        }
        if (!fei.hasNext()) {
            return true;
        }
        final Object feo = fei.next();
        while (ai.hasNext()) {
            final Object fao = ai.next();
            if (Objects.equals(fao, feo)) {
                final Iterator<Object> ei = expected.iterator();
                skipOne(ei);
                final boolean allMatches = continueMatchesOn(ai, ei);
                if (!ei.hasNext()) {
                    return allMatches;
                }
            }
        }
        return false;
    }

    protected static boolean continueMatchesOn(@Nonnull Iterator<Object> ai, @Nonnull Iterator<Object> ei) {
        boolean allMatches = true;
        while (ai.hasNext() && ei.hasNext()) {
            final Object ao = ai.next();
            final Object eo = ei.next();
            if (!Objects.equals(ao, eo)) {
                allMatches = false;
                break;
            }
        }
        return allMatches;
    }

    protected static void skipOne(@Nonnull Iterator<?> iterator) {
        if (!iterator.hasNext()) {
            throw new IllegalStateException();
        }
        iterator.next(); // Skip
    }

}
