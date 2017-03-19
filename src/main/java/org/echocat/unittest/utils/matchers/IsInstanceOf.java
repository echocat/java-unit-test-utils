package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

public class IsInstanceOf<T> extends org.hamcrest.core.IsInstanceOf {

    @Nonnull
    private final Class<T> expectedType;

    @Nonnull
    public static <T> Matcher<T> isInstanceOf(@Nonnull Class<?> expectedType) {
        // noinspection unchecked
        return (Matcher<T>) new IsInstanceOf(expectedType);
    }

    @Nonnull
    public static <T> Matcher<T> instanceOf(@Nonnull Class<?> expectedType) {
        return isInstanceOf(expectedType);
    }

    @Nonnull
    public static <T> Matcher<T> any(@Nonnull Class<T> expectedType) {
        return isInstanceOf(expectedType);
    }

    protected IsInstanceOf(@Nonnull Class<T> expectedType) {
        super(expectedType);
        this.expectedType = expectedType;
    }

    @Nonnull
    protected Class<T> expectedType() {
        return expectedType;
    }

}
