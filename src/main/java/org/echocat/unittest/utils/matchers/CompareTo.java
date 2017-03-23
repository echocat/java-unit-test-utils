package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.utils.ClassUtils.typeOf;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CompareTo<T extends Comparable<T>> extends TypeSafeMatcher<T> {

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isGreaterThan(@Nonnull T expected) {
        return new CompareTo<>(expected, "is greater than", greaterThanComparator());
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isGreaterThanOrEqualTo(@Nonnull T expected) {
        return new CompareTo<>(expected, "is greater than or equal to", greaterThanOrEqualToComparator());
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isLessThan(@Nonnull T expected) {
        return new CompareTo<>(expected, "is less than", lessThanComparator());
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isLessThanOrEqualTo(@Nonnull T expected) {
        return new CompareTo<>(expected, "is less than or equal to", lessThanOrEqualToComparator());
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> greaterThan(@Nonnull T expected) {
        return isGreaterThan(expected);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> greaterThanOrEqualTo(@Nonnull T expected) {
        return isGreaterThanOrEqualTo(expected);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> lessThan(@Nonnull T expected) {
        return isLessThan(expected);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> lessThanOrEqualTo(@Nonnull T expected) {
        return isLessThanOrEqualTo(expected);
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> greaterThanComparator() {
        return (actual, exp) -> actual.compareTo(exp) > 0;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> greaterThanOrEqualToComparator() {
        return (actual, exp) -> actual.compareTo(exp) >= 0;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> lessThanComparator() {
        return (actual, exp) -> actual.compareTo(exp) < 0;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> lessThanOrEqualToComparator() {
        return (actual, exp) -> actual.compareTo(exp) <= 0;
    }

    @Nonnull
    private final T expected;
    @Nonnull
    private final String comparatorDescription;
    @Nonnull
    private final Comparator<T> comparator;

    protected CompareTo(@Nonnull T expected, @Nonnull String comparatorDescription, @Nonnull Comparator<T> comparator) {
        super(typeOf(Comparable.class, expected));
        this.expected = expected;
        this.comparatorDescription = comparatorDescription;
        this.comparator = comparator;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return comparator.check(item, expected);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText(comparatorDescription).appendText(" ").appendValue(expected);
    }

    @Nonnull
    protected T expected() {
        return expected;
    }

    @Nonnull
    protected String comparatorDescription() {
        return comparatorDescription;
    }

    @Nonnull
    protected Comparator<T> comparator() {
        return comparator;
    }

    @FunctionalInterface
    public interface Comparator<T extends Comparable> {
        boolean check(@Nonnull T actual, @Nonnull T expected);
    }

}
