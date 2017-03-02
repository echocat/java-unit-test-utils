package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsSame;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsSameAs<T> extends IsSame<T> {

    @Nonnull
    public static <T> Matcher<T> isSameAs(@Nullable final T exepcted) {
        return new IsSameAs<>(exepcted);
    }

    @Nonnull
    public static <T> Matcher<T> isSame(@Nullable final T expected) {
        return isSameAs(expected);
    }

    @Nonnull
    public static <T> Matcher<T> isSameInstance(@Nullable final T expected) {
        return isSameAs(expected);
    }

    protected IsSameAs(@Nullable T expected) {
        super(expected);
    }
}
