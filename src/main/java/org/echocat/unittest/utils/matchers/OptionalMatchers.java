package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import java.util.Optional;

import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isAbsentBaseChecker;
import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isPresentBaseChecker;

public final class OptionalMatchers {

    @Nonnull
    public static <T> Matcher<Optional<T>> isPresent() {
        return new OptionalBasedMatcher<>("is present", isPresentBaseChecker(), null);
    }

    @Nonnull
    public static <T> Matcher<Optional<T>> isAbsent() {
        return new OptionalBasedMatcher<>("is absent", isAbsentBaseChecker(), null);
    }

    @Nonnull
    public static <T> Matcher<Optional<T>> whereContentMatches(@Nonnull Matcher<T> contentMatcher) {
        return new OptionalBasedMatcher<>("is present and content ", isPresentBaseChecker(), contentMatcher);
    }

}
