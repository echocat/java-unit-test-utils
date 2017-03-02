package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;

public abstract class StreamBasedMatcherSupport<V, T> extends BaseMatcher<V> {

    @Nonnegative
    protected static final int MAXIMUM_MISMATCHES_TO_REPORT = 10;
    @Nonnull
    private final Function<V, Stream<T>> mapper;
    @Nonnull
    private final Iterable<Matcher<T>> matchers;

    protected StreamBasedMatcherSupport(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Matcher<T> firstMatcher, @Nullable Matcher<T>[] otherMatchers) {
        this(mapper, collectMatchers(firstMatcher, otherMatchers));
    }

    protected StreamBasedMatcherSupport(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<Matcher<T>> matchers) {
        this.mapper = mapper;
        this.matchers = matchers;
    }

    @Nonnull
    protected static <T> Iterable<Matcher<T>> collectMatchers(@Nonnull Matcher<T> firstMatcher, @Nullable Matcher<T>[] otherMatchers) {
        final List<Matcher<T>> result = new ArrayList<>(1 + (otherMatchers != null ? otherMatchers.length : 0));
        result.add(firstMatcher);
        if (otherMatchers != null) {
            addAll(result, otherMatchers);
        }
        return unmodifiableList(result);
    }

    @Nonnull
    protected Function<V, Stream<T>> mapper() {
        return mapper;
    }

    @Nonnull
    protected Iterable<Matcher<T>> matchers() {
        return matchers;
    }

    @Override
    public boolean matches(@Nullable Object item) {
        if (item == null) {
            return false;
        }
        //noinspection unchecked
        return matches(mapper.apply((V) item));
    }

    protected abstract boolean matches(@Nonnull Stream<T> items);

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        if (item == null) {
            description.appendText("is null");
            return;
        }
        //noinspection unchecked
        describeMismatch(mapper.apply((V) item), description);
    }

    protected void describeMismatch(@Nonnull Stream<T> items, @Nonnull Description description) {
        final AtomicBoolean firstElementPrinted = new AtomicBoolean();
        final AtomicLong printedMismatchedElements = new AtomicLong();
        items.forEach(item -> {
            final AtomicBoolean firstMatcherPrinted = new AtomicBoolean();
            if (printedMismatchedElements.get() < MAXIMUM_MISMATCHES_TO_REPORT) {
                for (final Matcher<T> matcher : matchers()) {
                    if (!matcher.matches(item)) {
                        if (firstElementPrinted.getAndSet(true)) {
                            description.appendText("\n          ");
                        }
                        if (firstMatcherPrinted.getAndSet(true)) {
                            description.appendText("    and ");
                        } else {
                            description.appendText("for ").appendValue(item).appendText(" ");
                            printedMismatchedElements.incrementAndGet();
                        }
                        description.appendDescriptionOf(matcher).appendText(" ");
                        matcher.describeMismatch(item, description);
                    }
                }
            } else if (printedMismatchedElements.get() < MAXIMUM_MISMATCHES_TO_REPORT + 1) {
                description.appendText("\n          [...]");
                printedMismatchedElements.incrementAndGet();
            }
        });
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        final AtomicBoolean firstMatcherPrinted = new AtomicBoolean();
        matchers().forEach(matcher -> {
            if (firstMatcherPrinted.getAndSet(true)) {
                description.appendText("\n              and ");
            } else {
                description.appendText(description()).appendText(" ");
            }
            description.appendDescriptionOf(matcher);
        });
    }

    protected abstract String description();

}
