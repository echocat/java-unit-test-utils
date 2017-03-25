package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContainsAtLeastOneElementThat<V, T> extends CombinedMappingMatcher<V, T> {

    public ContainsAtLeastOneElementThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<? extends Matcher<T>> matchers) {
        super(mapper, matchers, streamMatcherInstance(), "contains at least one element that");
    }

    @Nonnull
    protected static <T> StreamMatcher<T> streamMatcherInstance() {
        return ContainsAtLeastOneElementThat::matches;
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
