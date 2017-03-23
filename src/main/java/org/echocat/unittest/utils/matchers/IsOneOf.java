package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import static java.util.Arrays.asList;

public class IsOneOf<T> extends BaseMatcher<T> {

    @Nonnull
    private final Collection<T> expected;

    @SafeVarargs
    @Nonnull
    public static <T> Matcher<T> isOneOf(@Nonnull T... expected) {
        return isOneOf(asList(expected));
    }

    @Nonnull
    public static <T> Matcher<T> isOneOf(@Nonnull Collection<T> expected) {
        return new IsOneOf<>(expected);
    }

    @SafeVarargs
    @Nonnull
    public static <T> Matcher<T> isAnyOf(@Nonnull T... expected) {
        return isAnyOf(asList(expected));
    }

    @Nonnull
    public static <T> Matcher<T> isAnyOf(@Nonnull Collection<T> expected) {
        return isOneOf(expected);
    }

    protected IsOneOf(@Nonnull Collection<T> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        //noinspection SuspiciousMethodCalls
        return expected.contains(item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("is one of").appendText(" [");
        boolean first = true;
        for (final T item : expected) {
            if (first) {
                first = false;
            } else {
                description.appendText(", ");
            }
            description.appendValue(item);
        }
        description.appendText("]");
    }

    @Nonnull
    protected Collection<T> expected() {
        return expected;
    }

}
