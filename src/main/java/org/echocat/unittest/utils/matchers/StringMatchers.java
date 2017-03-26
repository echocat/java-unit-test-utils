package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

public interface StringMatchers {

    @Nonnull
    static <T extends CharSequence> Matcher<T> startsWith(@Nonnull CharSequence prefix) {
        return new StringBasedMatcher<>("starts with", StringBasedMatcher.startsWithComparator(), prefix.toString());
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> endsWith(@Nonnull CharSequence suffix) {
        return new StringBasedMatcher<>("ends with", StringBasedMatcher.endsWithComparator(), suffix.toString());
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> contains(@Nonnull CharSequence what) {
        return new StringBasedMatcher<>("contains", StringBasedMatcher.containsComparator(), what.toString());
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> matches(@Nonnull CharSequence regex) {
        return new StringBasedMatcher<>("matches regular expression", StringBasedMatcher.matchesComparator(), regex.toString());
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> equalsIgnoreCase(@Nonnull CharSequence what) {
        return new StringBasedMatcher<>("equals ignore case", StringBasedMatcher.equalsIgnoreCaseComparator(), what.toString());
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> isEqualIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> isEqualToIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

    @Nonnull
    static <T extends CharSequence> Matcher<T> equalsToIgnoreCase(@Nonnull CharSequence what) {
        return equalsIgnoreCase(what);
    }

}
