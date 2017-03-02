package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public class IsEqualTo<T> extends IsEqual<T> {

    @Nonnull
    public static <T> Matcher<T> equalTo(@Nullable final T exepcted) {
        return new IsEqualTo<>(exepcted);
    }

    @Nonnull
    public static <T> Matcher<T> isEqualTo(@Nullable final T expected) {
        return equalTo(expected);
    }

    @Nonnull
    public static <T> Matcher<T> is(@Nullable final T expected) {
        return equalTo(expected);
    }

    @Nonnull
    public static Matcher<Boolean> isTrue() {
        return equalTo(true);
    }

    @Nonnull
    public static Matcher<Boolean> isFalse() {
        return equalTo(false);
    }

    protected IsEqualTo(@Nullable T expected) {
        super(expected);
    }
}
