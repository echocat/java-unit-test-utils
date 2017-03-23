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

public class ContainsOnlyElementsThat<V, T> extends CombinedMappingMatcher<V, T> {

    public interface Streams {
        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsOnlyElementsThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Stream<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Iterables {
        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsOnlyElementsThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterable<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Iterators {
        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsOnlyElementsThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Iterator<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Spliterators {
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsOnlyElementsThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<Spliterator<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    public interface Arrays {
        @Nonnull
        static <T> Matcher<T[]> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
        }

        @Nonnull
        static <T> Matcher<T[]> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
            return containsOnlyElementsThat(matchers);
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
        }

        @SafeVarargs
        @Nonnull
        static <T> Matcher<T[]> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
            return containsOnlyElementsThat(matcher, otherMatchers);
        }
    }

    protected ContainsOnlyElementsThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<? extends Matcher<T>> matchers) {
        super(mapper, matchers, ContainsOnlyElementsThat::matches, "contains only elements that");
    }

    protected static <T> boolean matches(@Nonnull Iterable<? extends Matcher<T>> matchers, @Nonnull Stream<T> items) {
        final AtomicBoolean everyItemWasTrue = new AtomicBoolean(true);
        final AtomicBoolean hasAtLeastOneItem = new AtomicBoolean(false);
        items.forEach(item -> matchers.forEach(matcher -> {
            hasAtLeastOneItem.set(true);
            if (!matcher.matches(item)) {
                everyItemWasTrue.set(false);
            }
        }));
        return everyItemWasTrue.get() && hasAtLeastOneItem.get();
    }

}
