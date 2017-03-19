package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.matchers.IsNot.not;

public class IsNull<T> extends org.hamcrest.core.IsNull<T> {

    @Nonnull
    public static Matcher<Object> nullValue() {
        return new IsNull<>();
    }

    @Nonnull
    public static Matcher<Object> isNull() {
        return nullValue();
    }

    @Nonnull
    public static Matcher<Object> isNullValue() {
        return nullValue();
    }

    @Nonnull
    public static Matcher<Object> notNullValue() {
        return not(nullValue());
    }

    @Nonnull
    public static Matcher<Object> isNotNull() {
        return notNullValue();
    }

    @Nonnull
    public static Matcher<Object> isNotNullValue() {
        return notNullValue();
    }

    protected IsNull() {
    }

}
