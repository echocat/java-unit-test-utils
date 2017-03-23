package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContainsAtLeastOneElementThat<V, T> extends CombinedMappingMatcher<V, T> {

    public interface Streams {
        @Nonnull
        static <T> Matcher<Stream<T>> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Stream<T>> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsAtLeastOneElementThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(matcher, otherMatchers);
        }
    }

    public interface Iterables {
        @Nonnull
        static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Iterable<T>> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsAtLeastOneElementThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(matcher, otherMatchers);
        }
    }

    public interface Iterators {
        @Nonnull
        static <T> Matcher<Iterator<T>> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Iterator<T>> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsAtLeastOneElementThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(matcher, otherMatchers);
        }
    }

    public interface Spliterators {
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Spliterator<T>> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsAtLeastOneElementThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(matcher, otherMatchers);
        }
    }

    public interface Arrays {
        @Nonnull
        static <T> Matcher<T[]> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<T[]> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsAtLeastOneElementThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsAtLeastOneElementThat(matcher, otherMatchers);
        }
    }

    @SafeVarargs
    @Nonnull
    public static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return Iterables.containsAtLeastOneElementThat(matcher, otherMatchers);
    }

    protected ContainsAtLeastOneElementThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<? extends Matcher<T>> matchers) {
        super(mapper, matchers, ContainsAtLeastOneElementThat::matches, "contains at least one element that");
    }

    protected static <T> boolean matches(@Nonnull Iterable<? extends Matcher<T>> matchers, @Nonnull Stream<T> items) {
        final AtomicBoolean atLeastOneRowMatches = new AtomicBoolean(false);
        items.forEach(item -> {
            final AtomicBoolean allMatchersMatches = new AtomicBoolean(true);
            matchers.forEach(matcher -> {
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

}
