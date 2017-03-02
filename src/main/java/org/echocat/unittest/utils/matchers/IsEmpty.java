package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
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
        if (item == null) {
            return true;
        }
        if (item instanceof Collection) {
            return ((Collection<?>)item).isEmpty();
        }
        if (item instanceof Iterable) {
            return !((Iterable<?>)item).iterator().hasNext();
        }
        if (item instanceof Iterator) {
            return !((Iterator<?>)item).hasNext();
        }
        if (item instanceof Stream) {
            //noinspection OverlyStrongTypeCast
            return !((Stream<?>)item).iterator().hasNext();
        }
        if (item instanceof Map) {
            return ((Map<?, ?>)item).isEmpty();
        }
        if (item instanceof Object[]) {
            return ((Object[])item).length == 0;
        }
        if (item instanceof CharSequence) {
            return ((CharSequence)item).length() == 0;
        }
        throw new IllegalArgumentException("Could not handle an argument " + item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("is ").appendValue("empty");
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        description.appendText("has items: ").appendValue(item);
    }

}
