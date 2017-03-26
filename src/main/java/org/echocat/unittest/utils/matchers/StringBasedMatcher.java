package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringBasedMatcher<T extends CharSequence> extends BaseMatcher<T> {

    @Nonnull
    protected static Comparator startsWithComparator() {
        return String::startsWith;
    }

    @Nonnull
    protected static Comparator endsWithComparator() {
        return String::endsWith;
    }

    @Nonnull
    protected static Comparator containsComparator() {
        return String::contains;
    }

    @Nonnull
    protected static Comparator matchesComparator() {
        return String::matches;
    }

    @Nonnull
    protected static Comparator equalsIgnoreCaseComparator() {
        return String::equalsIgnoreCase;
    }

    @Nonnull
    private final String expected;
    @Nonnull
    private final String comparatorDescription;
    @Nonnull
    private final Comparator comparator;

    protected StringBasedMatcher(@Nonnull String comparatorDescription, @Nonnull Comparator comparator, @Nonnull String expected) {
        this.comparatorDescription = comparatorDescription;
        this.comparator = comparator;
        this.expected = expected;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        if (item instanceof CharSequence) {
            return comparator.check(item.toString(), expected);
        }
        return false;
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText(comparatorDescription).appendText(" ").appendValue(expected);
    }

    @Nonnull
    protected String expected() {
        return expected;
    }

    @Nonnull
    protected String comparatorDescription() {
        return comparatorDescription;
    }

    @Nonnull
    protected Comparator comparator() {
        return comparator;
    }

    @FunctionalInterface
    public interface Comparator {
        boolean check(@Nonnull String actual, @Nonnull String expected);
    }

}
