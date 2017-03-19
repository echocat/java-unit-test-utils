package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsSame;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsSameAs<T> extends IsSame<T> {

    @Nullable
    private final T expected;

    @Nonnull
    public static <T> Matcher<T> isSameAs(@Nullable T exepcted) {
        return new IsSameAs<>(exepcted);
    }

    @Nonnull
    public static <T> Matcher<T> isSame(@Nullable T expected) {
        return isSameAs(expected);
    }

    @Nonnull
    public static <T> Matcher<T> isSameInstance(@Nullable T expected) {
        return isSameAs(expected);
    }

    @Nonnull
    public static <T> Matcher<T> sameInstance(@Nullable T expected) {
        return isSameAs(expected);
    }

    protected IsSameAs(@Nullable T expected) {
        super(expected);
        this.expected = expected;
    }

    @Nullable
    protected T expected() {
        return expected;
    }

}
