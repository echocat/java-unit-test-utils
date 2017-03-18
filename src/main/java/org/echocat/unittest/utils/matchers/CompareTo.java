package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;

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
    private static final Comparator<Comparable> GREATER_THAN_COMPARATOR = (actual, exp) -> actual.compareTo(exp) > 0;
    @Nonnull
    private static final Comparator<Comparable> GREATER_THAN_OR_EQUAL_TO_COMPARATOR = (actual, exp) -> actual.compareTo(exp) >= 0;
    @Nonnull
    private static final Comparator<Comparable> LESS_THAN_COMPARATOR = (actual, exp) -> actual.compareTo(exp) < 0;
    @Nonnull
    private static final Comparator<Comparable> LESS_THAN_OR_EQUAL_TO_COMPARATOR = (actual, exp) -> actual.compareTo(exp) <= 0;

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> greaterThanComparator() {
        //noinspection RedundantCast
        return (Comparator<T>) (Object) GREATER_THAN_COMPARATOR;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> greaterThanOrEqualToComparator() {
        //noinspection RedundantCast
        return (Comparator<T>) (Object) GREATER_THAN_OR_EQUAL_TO_COMPARATOR;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> lessThanComparator() {
        //noinspection RedundantCast
        return (Comparator<T>) (Object) LESS_THAN_COMPARATOR;
    }

    @Nonnull
    protected static <T extends Comparable<T>> Comparator<T> lessThanOrEqualToComparator() {
        //noinspection RedundantCast
        return (Comparator<T>) (Object) LESS_THAN_OR_EQUAL_TO_COMPARATOR;
    }

    @Nonnull
    private final T expected;
    @Nonnull
    private final String comparatorDescription;
    @Nonnull
    private final Comparator<T> comparator;

    protected CompareTo(@Nonnull T expected, @Nonnull String comparatorDescription, @Nonnull Comparator<T> comparator) {
        super(typeOf(expected));
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

    @Nonnull
    protected static Class<?> typeOf(@Nonnull Object expected) {
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
    public interface Comparator<T extends Comparable> {
        boolean check(@Nonnull T actual, @Nonnull T expected);
    }

}
