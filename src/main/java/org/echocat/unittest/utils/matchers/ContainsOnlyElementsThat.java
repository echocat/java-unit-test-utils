package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;

public class ContainsOnlyElementsThat<V, T> extends CombinedMappingMatcher<V, T> {

    public ContainsOnlyElementsThat(@Nonnull Function<V, Stream<T>> mapper, @Nonnull Iterable<? extends Matcher<T>> matchers) {
        super(mapper, matchers, streamMatcherInstance(), "contains only elements that");
    }

    @Nonnull
    protected static <T> StreamMatcher<T> streamMatcherInstance() {
        return ContainsOnlyElementsThat::matches;
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
