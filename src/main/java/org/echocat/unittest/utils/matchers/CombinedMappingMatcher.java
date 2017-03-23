package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;

public class CombinedMappingMatcher<V, T> extends BaseMatcher<V> {

    @Nonnegative
    protected static final int MAXIMUM_MISMATCHES_TO_REPORT = 10;
    @Nonnull
    private final Function<V, Stream<T>> mapper;
    @Nonnull
    private final Iterable<? extends Matcher<T>> matchers;
    @Nonnull
    private final StreamMatcher<T> streamMatcher;
    @Nonnull
    private final String description;

    public CombinedMappingMatcher(@Nonnull Function<V, Stream<T>> mapper,
                                  @Nonnull Iterable<? extends Matcher<T>> matchers,
                                  @Nonnull StreamMatcher<T> streamMatcher,
                                  @Nonnull String description
    ) {
        this.mapper = mapper;
        this.matchers = matchers;
        this.streamMatcher = streamMatcher;
        this.description = description;
    }

    @SafeVarargs
    @Nonnull
    public static <T> Iterable<Matcher<T>> collectMatchers(@Nonnull Matcher<T> firstMatcher, @Nullable Matcher<T>... otherMatchers) {
        final List<Matcher<T>> result = new ArrayList<>(1 + (otherMatchers != null ? otherMatchers.length : 0));
        result.add(firstMatcher);
        if (otherMatchers != null) {
            addAll(result, otherMatchers);
        }
        return unmodifiableList(result);
    }

    @Override
    public boolean matches(@Nullable Object item) {
        if (item == null) {
            return false;
        }
        final Stream<T> stream;
        try {
            //noinspection unchecked
            stream = mapper().apply((V) item);
        } catch (final ClassCastException ignored) {
            return false;
        }
        return streamMatcher().matches(matchers(), stream);
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        if (item == null) {
            description.appendText("was ").appendValue(null);
            return;
        }
        final Stream<T> stream;
        try {
            //noinspection unchecked
            stream = mapper().apply((V) item);
        } catch (final ClassCastException ignored) {
            //noinspection ConstantConditions
            description.appendText("was unexpected type ").appendValue(item.getClass());
            return;
        }
        //noinspection unchecked
        describeMismatch(stream, description);
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

    @Nonnull
    protected Function<V, Stream<T>> mapper() {
        return mapper;
    }

    @Nonnull
    protected Iterable<? extends Matcher<T>> matchers() {
        return matchers;
    }

    @Nonnull
    protected StreamMatcher<T> streamMatcher() {
        return streamMatcher;
    }

    @Nonnull
    protected String description() {
        return description;
    }

    @FunctionalInterface
    public interface StreamMatcher<T> {
        boolean matches(@Nonnull Iterable<? extends Matcher<T>> matchers, @Nonnull Stream<T> input);
    }

}
