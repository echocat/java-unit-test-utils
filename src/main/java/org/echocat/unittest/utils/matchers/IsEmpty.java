package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.SizeUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsEmpty extends BaseMatcher<Object> {

    @Nonnull
    private static final IsEmpty INSTANCE = new IsEmpty();

    @Nonnull
    public static <T> Matcher<T> isEmpty() {
        //noinspection unchecked
        return (Matcher<T>) INSTANCE;
    }

    @Nonnull
    public static <T> Matcher<T> empty() {
        return isEmpty();
    }

    @Nonnull
    public static <T> Matcher<T> hasNoItems() {
        return isEmpty();
    }

    protected IsEmpty() {
    }

    @Override
    public boolean matches(@Nullable Object item) {
        return SizeUtils.isEmpty(item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("is <empty>");
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("has items: ").appendValue(item);
    }

}
