package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsSame;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsSameAs<T> extends BaseMatcher<T> {

    @Nullable
    private final T expected;
    @Nonnull
    private final String matcherName;

    @Nonnull
    public static <T> Matcher<T> isSameAs(@Nullable T expected) {
        return new IsSameAs<>(expected, "is same as");
    }

    @Nonnull
    public static <T> Matcher<T> isSame(@Nullable T expected) {
        return new IsSameAs<>(expected, "is same");
    }

    @Nonnull
    public static <T> Matcher<T> isSameInstance(@Nullable T expected) {
        return new IsSameAs<>(expected, "is same instance");
    }

    @Nonnull
    public static <T> Matcher<T> sameAs(@Nullable T expected) {
        return new IsSameAs<>(expected, "same as");
    }

    @Nonnull
    public static <T> Matcher<T> same(@Nullable T expected) {
        return new IsSameAs<>(expected, "same");
    }

    @Nonnull
    public static <T> Matcher<T> sameInstance(@Nullable T expected) {
        return new IsSameAs<>(expected, "same instance");
    }

    protected IsSameAs(@Nullable T expected, @Nonnull String matcherName) {
        this.expected = expected;
        this.matcherName = matcherName;
    }

    @Nullable
    protected T expected() {
        return expected;
    }

    @Nonnull
    protected String matcherName() {
        return matcherName;
    }

    @Override
    public boolean matches(Object arg) {
        //noinspection ObjectEquality
        return arg == expected();
    }

    @Override
    public void describeTo(Description description) {
        description
            .appendText(matcherName())
            .appendText(" ")
            .appendValue(expected());
    }

}
