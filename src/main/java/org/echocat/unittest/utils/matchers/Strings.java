package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Strings<T extends CharSequence> extends BaseMatcher<T> {

    @Nonnull
    public static <T extends CharSequence> Matcher<T> startsWith(@Nonnull CharSequence prefix) {
        return new Strings<>("starts with", startsWithComparator(), prefix.toString());
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> endsWith(@Nonnull CharSequence suffix) {
        return new Strings<>("ends with", endsWithComparator(), suffix.toString());
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> contains(@Nonnull CharSequence what) {
        return new Strings<>("contains", containsComparator(), what.toString());
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> matches(@Nonnull CharSequence regex) {
        return new Strings<>("matches regular expression", matchesComparator(), regex.toString());
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> equalsIgnoreCase(@Nonnull CharSequence what) {
        return new Strings<>("equals ignore case", equalsIgnoreCaseComparator(), what.toString());
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> isEqualIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> isEqualToIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

    @Nonnull
    public static <T extends CharSequence> Matcher<T> equalsToIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

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

    protected Strings(@Nonnull String comparatorDescription, @Nonnull Comparator comparator, @Nonnull String expected) {
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
