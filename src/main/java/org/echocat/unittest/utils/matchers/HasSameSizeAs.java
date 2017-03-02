package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.SizeUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Collections.synchronizedMap;

public class HasSameSizeAs<T> extends BaseMatcher<T> {

    @Nonnull
    public static <T> Matcher<T> hasSameSizeAs(@Nullable T compareTo) {
        return new HasSameSizeAs<>(compareTo);
    }

    @Nonnull
    public static <T> Matcher<T> hasSameLengthAs(@Nullable T compareTo) {
        return hasSameSizeAs(compareTo);
    }

    @Nullable
    private final T compareTo;
    @Nonnull
    private final Map<Object, Long> itemToSize = synchronizedMap(new WeakHashMap<>(1, 1));

    protected HasSameSizeAs(@Nullable T compareTo) {
        this.compareTo = compareTo;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        final long actualSize = itemToSize.computeIfAbsent(item, SizeUtils::sizeOf);
        final long expectedSize = itemToSize.computeIfAbsent(compareTo, SizeUtils::sizeOf);
        return actualSize == expectedSize;
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("has size ").appendValue(itemToSize.get(compareTo)).appendText(" same as ").appendValue(compareTo);
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("was ").appendValue(itemToSize.get(item)).appendText(" with content ").appendValue(item);
    }

}
