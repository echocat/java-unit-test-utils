package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.SizeUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Collections.synchronizedMap;

public class HasSize<T> extends BaseMatcher<T> {

    @Nonnull
    public static <T> Matcher<T> hasSize(@Nonnegative long size) {
        return new HasSize<>(size);
    }

    @Nonnull
    public static <T> Matcher<T> hasSizeOf(@Nonnegative long size) {
        return hasSize(size);
    }

    @Nonnull
    public static <T> Matcher<T> hasLength(@Nonnegative long size) {
        return hasSize(size);
    }

    @Nonnull
    public static <T> Matcher<T> hasLengthOf(@Nonnegative long size) {
        return hasSize(size);
    }

    @Nonnegative
    private final long expectedSize;
    @Nonnull
    private final Map<Object, Long> itemToSize = synchronizedMap(new WeakHashMap<>(1, 1));

    protected HasSize(@Nonnegative long expectedSize) {
        this.expectedSize = expectedSize;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        final long size = itemToSize.computeIfAbsent(item, SizeUtils::sizeOf);
        return size == expectedSize;
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("has size of ").appendValue(expectedSize);
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("was ").appendValue(itemToSize.get(item)).appendText(" with content ").appendValue(item);
    }

}
