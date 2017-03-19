package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;

public class IsNot<T> extends org.hamcrest.core.IsNot<T> {

    @Nonnull
    public static <T> Matcher<T> not(@Nonnull Matcher<T> enclosed) {
        return new IsNot<>(enclosed);
    }

    @Nonnull
    public static <T> Matcher<T> isNot(@Nonnull Matcher<T> enclosed) {
        return not(enclosed);
    }

    @Nonnull
    public static <T> Matcher<T> not(@Nonnull T expected) {
        return not(equalTo(expected));
    }

    @Nonnull
    public static <T> Matcher<T> isNot(@Nonnull T expected) {
        return not(expected);
    }

    @Nonnull
    public static <T> Matcher<T> notEqualTo(@Nonnull T expected) {
        return not(expected);
    }

    @Nonnull
    public static <T> Matcher<T> isNotEqualTo(@Nonnull T expected) {
        return not(expected);
    }

    @Nonnull
    private final Matcher<T> enclosed;

    protected IsNot(@Nonnull Matcher<T> matcher) {
        super(matcher);
        enclosed = matcher;
    }

    @Nonnull
    protected Matcher<T> enclosed() {
        return enclosed;
    }

}
