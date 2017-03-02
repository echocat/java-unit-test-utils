package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;

public class CompareTo<T extends Comparable<T>> extends TypeSafeMatcher<T> {

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isGreatherThan(@Nonnull T expected) {
        return new CompareTo<>(expected, "is greather than", (actual, exp) -> actual.compareTo(exp) > 0);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isGreatherThanOrEqualTo(@Nonnull T expected) {
        return new CompareTo<>(expected, "is greather than or equal to", (actual, exp) -> actual.compareTo(exp) >= 0);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isLessThan(@Nonnull T expected) {
        return new CompareTo<>(expected, "is less than", (actual, exp) -> actual.compareTo(exp) < 0);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> isLessThanOrEqualTo(@Nonnull T expected) {
        return new CompareTo<>(expected, "is less than or equal to", (actual, exp) -> actual.compareTo(exp) <= 0);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> greatherThan(@Nonnull T expected) {
        return isGreatherThan(expected);
    }

    @Nonnull
    public static <T extends Comparable<T>> Matcher<T> greatherThanOrEqualTo(@Nonnull T expected) {
        return isGreatherThanOrEqualTo(expected);
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
    private final T expected;
    @Nonnull
    private final String comparatorDescription;
    @Nonnull
    private final Comparator<T> comparator;

    public CompareTo(@Nonnull T expected, @Nonnull String comparatorDescription, @Nonnull Comparator<T> comparator) {
        super(getTypeOf(expected));
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
    protected static Class<?> getTypeOf(@Nonnull Object expected) {
        //noinspection ConstantConditions
        if (expected == null) {
            throw new NullPointerException("The provided expected value is null.");
        }
        if (!(expected instanceof Comparable)) {
            throw new IllegalArgumentException("The provided expected value is not of type " + Comparable.class.getName() + ".");
        }
        return expected.getClass();
    }

    @FunctionalInterface
    public interface Comparator<T extends Comparable<T>> {
        boolean check(@Nonnull T actual, @Nonnull T expected);
    }

}
