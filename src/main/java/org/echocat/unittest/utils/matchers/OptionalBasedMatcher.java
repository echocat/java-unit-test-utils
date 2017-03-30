package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class OptionalBasedMatcher<T> extends BaseMatcher<Optional<T>> {

    @Nonnull
    protected static <T> BaseChecker<T> isPresentBaseChecker() {
        return Optional::isPresent;
    }

    @Nonnull
    protected static <T> BaseChecker<T> isAbsentBaseChecker() {
        return actual -> !actual.isPresent();
    }

    @Nonnull
    private final String baseDescription;
    @Nonnull
    private final BaseChecker<T> baseChecker;
    @Nonnull
    private final Optional<Matcher<T>> contentMatcher;

    protected OptionalBasedMatcher(@Nonnull String baseDescription, @Nonnull BaseChecker<T> baseChecker, @Nullable Matcher<T> contentMatcher) {
        this.baseDescription = baseDescription;
        this.baseChecker = baseChecker;
        this.contentMatcher = Optional.ofNullable(contentMatcher);
    }

    @Override
    public boolean matches(@Nullable Object item) {
        if (!(item instanceof Optional)) {
            return false;
        }
        //noinspection unchecked
        final Optional<T> optional = (Optional<T>) item;
        if (!baseChecker().check(optional)) {
            return false;
        }
        return contentMatcher()
            .map(matcher -> matcher.matches(
                optional.orElseThrow(() -> new IllegalStateException("The base checker should cover presence of optional value before.")))
            )
            .orElse(true);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText(baseDescription());
        contentMatcher().ifPresent(matcher -> matcher.describeTo(description));
    }

    @Nonnull
    protected String baseDescription() {
        return baseDescription;
    }

    @Nonnull
    protected BaseChecker<T> baseChecker() {
        return baseChecker;
    }

    @Nonnull
    protected Optional<Matcher<T>> contentMatcher() {
        return contentMatcher;
    }

    @FunctionalInterface
    public interface BaseChecker<T> {
        boolean check(@Nonnull Optional<T> actual);
    }

}
