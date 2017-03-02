package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Arrays.asList;

public class IsOneOf<T> extends BaseMatcher<T> {

    @Nonnull
    private final Collection<T> expected;

    @Nonnull
    public static <T> Matcher<T> isOneOf(@Nonnull final Collection<T> expected) {
        return new IsOneOf<>(expected);
    }

    @SuppressWarnings({"OverloadedVarargsMethod", "unchecked"})
    @Nonnull
    public static <T> Matcher<T> isOneOf(@Nonnull final T firstExpected, @Nullable final T... others) {
        final Set<T> them = new HashSet<>();
        them.add(firstExpected);
        if (others != null) {
            them.addAll(asList(others));
        }
        return isOneOf(them);
    }

    protected IsOneOf(@Nonnull Collection<T> expected) {
        if (expected.isEmpty()) {
            throw new IllegalArgumentException("There is no expected item provided.");
        }
        this.expected = expected;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        //noinspection SuspiciousMethodCalls
        return expected.contains(item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("is one of ").appendValue(expected);
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("was ").appendValue(item);
    }

}
