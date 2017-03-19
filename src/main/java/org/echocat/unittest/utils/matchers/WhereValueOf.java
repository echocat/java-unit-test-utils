package org.echocat.unittest.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.SizeUtils.isNotEmpty;

public class WhereValueOf<T, ST> extends BaseMatcher<T> {

    @Nonnull
    public static <T, ST> Matcher<T> whereValueOf(@Nonnull Function<T, ST> mapper, @Nonnull String mapperDescription, @Nonnull Matcher<ST> subMatcher) {
        return new WhereValueOf<>(mapper, mapperDescription, subMatcher);
    }

    @Nonnull
    public static <T, ST> Matcher<T> element(@Nonnull Function<T, ST> mapper, @Nonnull String mapperDescription, @Nonnull Matcher<ST> subMatcher) {
        return whereValueOf(mapper, mapperDescription, subMatcher);
    }

    @Nonnull
    public static <T, ST> Matcher<T> elementMatches(@Nonnull Function<T, ST> mapper, @Nonnull String mapperDescription, @Nonnull Matcher<ST> subMatcher) {
        return whereValueOf(mapper, mapperDescription, subMatcher);
    }

    @Nonnull
    public static <T, ST> Matcher<T> valueOfMatches(@Nonnull Function<T, ST> mapper, @Nonnull String mapperDescription, @Nonnull Matcher<ST> subMatcher) {
        return whereValueOf(mapper, mapperDescription, subMatcher);
    }

    @Nonnull
    private final Function<T, ST> mapper;
    @Nullable
    private final String mapperDescription;
    @Nonnull
    private final Matcher<ST> subMatcher;

    protected WhereValueOf(@Nonnull Function<T, ST> mapper, @Nullable String mapperDescription, @Nonnull Matcher<ST> subMatcher) {
        this.mapper = requireNonNull(mapper, "No mapper provided.");
        this.mapperDescription = mapperDescription;
        this.subMatcher = requireNonNull(subMatcher, "No sub matcher provided.");
    }

    @Override
    public boolean matches(@Nullable Object item) {
        //noinspection unchecked
        final ST subItem = mapper.apply((T) item);
        return subMatcher.matches(subItem);
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("where value of ").appendValue(isNotEmpty(mapperDescription) ? mapperDescription : mapper).appendText(" ");
        subMatcher.describeTo(description);
    }

    @Override
    public void describeMismatch(@Nullable Object item, @Nonnull Description description) {
        //noinspection unchecked
        final ST subItem = mapper.apply((T) item);
        subMatcher.describeMismatch(subItem, description);
    }

}
