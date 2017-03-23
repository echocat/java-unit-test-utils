package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
public class IsEqualTo<T> extends IsEqual<T> {

    @Nullable
    private final T expected;

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
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is equal to ").appendValue(expected());
    }

    @Nullable
    protected T expected() {
        return expected;
    }
}
