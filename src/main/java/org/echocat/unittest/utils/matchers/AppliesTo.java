package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class AppliesTo<T> extends TypeSafeMatcher<T> {

    @Nonnull
    public static <T> Matcher<T> appliesTo(@Nonnull Predicate<T> predicate) {
        return new AppliesTo<>(predicate);
    }

    @Nonnull
    public static <T> Matcher<T> apply(@Nonnull Predicate<T> predicate) {
        return appliesTo(predicate);
    }

    @Nonnull
    private final Predicate<T> predicate;

    protected AppliesTo(@Nonnull Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return predicate().test(item);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("applies to ").appendValue(predicate());
    }

    @Nonnull
    protected Predicate<T> predicate() {
        return predicate;
    }

}
