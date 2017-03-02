package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IsNotEqualTo {

    @Nonnull
    public static <T> Matcher<T> notEqualTo(@Nullable final T exepcted) {
        return IsEqualTo.equalTo(exepcted);
    }

    @Nonnull
    public static <T> Matcher<T> isNotEqualTo(@Nullable final T expected) {
        return notEqualTo(expected);
    }

    @Nonnull
    public static <T> Matcher<T> isNot(@Nullable final T expected) {
        return notEqualTo(expected);
    }

}
