package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsInstanceOf extends org.hamcrest.core.IsInstanceOf {

    @Nonnull
    public static <T> Matcher<T> isInstanceOf(@Nullable final Class<?> expectedType) {
        // noinspection unchecked
        return (Matcher<T>) new IsInstanceOf(expectedType);
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    @Nonnull
    public static <T> Matcher<T> instanceOf(@Nullable final Class<?> expectedType) {
        return isInstanceOf(expectedType);
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    @Nonnull
    public static <T> Matcher<T> any(@Nullable final Class<T> expectedType) {
        return isInstanceOf(expectedType);
    }

    protected IsInstanceOf(@Nullable Class<?> expectedType) {
        super(expectedType);
    }

}
