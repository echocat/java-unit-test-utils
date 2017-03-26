package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.CombinedMappingMatcher.collectMatchers;

public interface IterableMatchers {

    @Nonnull
    static <T> Matcher<Iterable<T>> containsOnlyElementsThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
        return new ContainsOnlyElementsThat<>(StreamUtils::toStream, matchers);
    }

    @SafeVarargs
    @Nonnull
    static <T> Matcher<Iterable<T>> containsOnlyElementsThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return containsOnlyElementsThat(collectMatchers(matcher, otherMatchers));
    }

    @Nonnull
    static <T> Matcher<Iterable<T>> containsOnlyElements(@Nonnull Iterable<? extends Matcher<T>> matchers) {
        return containsOnlyElementsThat(matchers);
    }

    @SafeVarargs
    @Nonnull
    static <T> Matcher<Iterable<T>> containsOnlyElements(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return containsOnlyElements(collectMatchers(matcher, otherMatchers));
    }

    @Nonnull
    static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Iterable<? extends Matcher<T>> matchers) {
        return new ContainsAtLeastOneElementThat<>(StreamUtils::toStream, matchers);
    }

    @SafeVarargs
    @Nonnull
    static <T> Matcher<Iterable<T>> containsAtLeastOneElementThat(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return containsAtLeastOneElementThat(collectMatchers(matcher, otherMatchers));
    }

    @Nonnull
    static <T> Matcher<Iterable<T>> containsAtLeastOneElement(@Nonnull Iterable<? extends Matcher<T>> matchers) {
        return containsAtLeastOneElementThat(matchers);
    }

    @SafeVarargs
    @Nonnull
    static <T> Matcher<Iterable<T>> containsAtLeastOneElement(@Nonnull Matcher<T> matcher, @Nullable Matcher<T>... otherMatchers) {
        return containsAtLeastOneElement(collectMatchers(matcher, otherMatchers));
    }

    @SafeVarargs
    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> startsWith(@Nonnull V... prefix) {
        return startsWith(asList(prefix));
    }

    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> startsWith(@Nonnull Iterable<? extends V> prefix) {
        return new IterableBasedMatcher<>("starts with", IterableBasedMatcher.startsWithComparator(), prefix);
    }

    @SafeVarargs
    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> endsWith(@Nonnull V... suffix) {
        return endsWith(asList(suffix));
    }

    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> endsWith(@Nonnull Iterable<? extends V> suffix) {
        return new IterableBasedMatcher<>("ends with", IterableBasedMatcher.endsWithComparator(), suffix);
    }

    @SafeVarargs
    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> contains(@Nonnull V... what) {
        return contains(asList(what));
    }

    @Nonnull
    static <V, T extends Iterable<V>> Matcher<T> contains(@Nonnull Iterable<? extends V> what) {
        return new IterableBasedMatcher<>("contains", IterableBasedMatcher.containsComparator(), what);
    }

}
