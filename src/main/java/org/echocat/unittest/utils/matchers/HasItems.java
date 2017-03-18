package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.echocat.unittest.utils.utils.SizeUtils.isEmpty;

public class HasItems extends BaseMatcher<Object> {

    @Nonnull
    private static final HasItems INSTANCE = new HasItems();

    @Nonnull
    public static <T> Matcher<T> hasItems() {
        //noinspection unchecked
        return (Matcher<T>) INSTANCE;
    }

    @Nonnull
    public static <T> Matcher<T> hasElements() {
        return hasItems();
    }

    @Nonnull
    public static <T> Matcher<T> isNotEmpty() {
        return hasItems();
    }

    protected HasItems() {
    }

    @Override
    public boolean matches(@Nullable Object item) {
        return !isEmpty(item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("has items");
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("was <empty>");
    }

}
