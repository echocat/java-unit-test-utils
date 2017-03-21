package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainsOnlyElementsThat<V, T> extends StreamBasedMatcherSupport<V, T> {

    public interface Streams {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Iterables {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Iterators {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Spliterators {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Arrays {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    protected ContainsOnlyElementsThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Matcher<T> firstMatcher, @Nullable Matcher<T>[] otherMatchers) {
        super(mapper, firstMatcher, otherMatchers);
    }

    protected ContainsOnlyElementsThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<Matcher<T>> matchers) {
        super(mapper, matchers);
    }

    @Override
    protected boolean matches(@Nonnull Stream<T> items) {
        final AtomicBoolean everyItemWasTrue = new AtomicBoolean(true);
        final AtomicBoolean hasAtLeastOneItem = new AtomicBoolean(false);
        items.forEach(item -> matchers().forEach(matcher -> {
            hasAtLeastOneItem.set(true);
            if (!matcher.matches(item)) {
                everyItemWasTrue.set(false);
            }
        }));
        return everyItemWasTrue.get() && hasAtLeastOneItem.get();
    }

    @Override
    protected String description() {
        return "contains only elements that";
    }

}
