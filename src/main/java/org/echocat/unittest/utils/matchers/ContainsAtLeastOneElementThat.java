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

public class ContainsAtLeastOneElementThat<V, T> extends StreamBasedMatcherSupport<V, T> {

    public interface Streams {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }
    }

    public interface Iterables {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }
    }

    public interface Iterators {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }
    }

    public interface Spliterators {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }
    }

    public interface Arrays {
        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matcher, otherMatchers);
        }
    }

    @SafeVarargs
    @Nonnull
    public static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return Iterables.containsAtLeastOneElementThat(matcher, otherMatchers);
    }

    protected ContainsAtLeastOneElementThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Matcher<T> firstMatcher, @Nullable Matcher<T>[] otherMatchers) {
        super(mapper, firstMatcher, otherMatchers);
    }

    protected ContainsAtLeastOneElementThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<Matcher<T>> matchers) {
        super(mapper, matchers);
    }

    @Override
    protected boolean matches(@Nonnull Stream<T> items) {
        final AtomicBoolean atLeastOneRowMatches = new AtomicBoolean(false);
        items.forEach(item -> {
            final AtomicBoolean allMatchersMatches = new AtomicBoolean(true);
            matchers().forEach(matcher -> {
                if (!matcher.matches(item)) {
                    allMatchersMatches.set(false);
                }
            });
            if (allMatchersMatches.get()) {
                atLeastOneRowMatches.set(true);
            }
        });
        return atLeastOneRowMatches.get();
    }

    @Override
    protected String description() {
        return "contains at least one element that";
    }

}
