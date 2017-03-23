package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.CombinedMappingMatcher.StreamMatcher;
import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.CombinedMappingMatcher.collectMatchers;
import static org.echocat.unittest.utils.matchers.CompareTo.isGreaterThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.CompareTo.isLessThanOrEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CombinedMappingMatcherUnitTest {

    @Nonnull
    protected static final Matcher<Integer> MATCHER_1 = isGreaterThanOrEqualTo(0);
    @Nonnull
    protected static final Matcher<Integer> MATCHER_2 = isLessThanOrEqualTo(10);
    @Nonnull
    protected static final List<Matcher<Integer>> MATCHERS = asList(MATCHER_1, MATCHER_2);
    @Nonnull
    protected static final Function<Iterable<Integer>, Stream<Integer>> MAPPER = StreamUtils::toStream;
    @Nonnull
    protected static final StreamMatcher<Integer> STREAM_MATCHER = (matchers, stream) -> {
        final Optional<Integer> first = stream.findFirst();
        if (!first.isPresent()) {
            return false;
        }
        for (final Matcher<Integer> matcher : matchers) {
            if (!matcher.matches(first.get())) {
                return false;
            }
        }
        return true;
    };

    @Test
    public void factoryMethodCollectMatchers() throws Exception {
        assertThat(collectMatchers(MATCHER_1), equalTo(singletonList(MATCHER_1)));
        assertThat(collectMatchers(MATCHER_1, MATCHER_2), equalTo(asList(MATCHER_1, MATCHER_2)));
    }

    @Test
    public void constructor() throws Exception {
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = new CombinedMappingMatcher<>(MAPPER, MATCHERS, STREAM_MATCHER, "first item");

        assertThat(instance.mapper(), sameInstance(MAPPER));
        assertThat(instance.matchers(), sameInstance(MATCHERS));
        assertThat(instance.streamMatcher(), sameInstance(STREAM_MATCHER));
        assertThat(instance.description(), equalTo("first item"));
    }

    @Test
    public void matches() throws Exception {
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        assertThat(instance.matches(singleton(0)), equalTo(true));
        assertThat(instance.matches(singleton(5)), equalTo(true));
        assertThat(instance.matches(singleton(10)), equalTo(true));

        assertThat(instance.matches(singleton(-1)), equalTo(false));
        assertThat(instance.matches(singleton(11)), equalTo(false));
        assertThat(instance.matches(singleton(5L)), equalTo(false));
        assertThat(instance.matches(0), equalTo(false));
        assertThat(instance.matches(null), equalTo(false));
    }

    @Test
    public void describeMismatchForNull() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch((Object) null, description);

        assertThat(description.toString(), equalTo("was null"));
    }

    @Test
    public void describeMismatchForIllegalType() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(1L, description);

        assertThat(description.toString(), equalTo("was unexpected type <" + Long.class + ">"));
    }

    @Test
    public void describeMismatchFor2MatchersAndOneMismatch() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(singleton(666), description);

        assertThat(description.toString(), equalTo("for <666> is less than or equal to <10> was <666>"));
    }

    @Test
    public void describeToFor2Matchers() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("first item is greater than or equal to <0>\n" +
            "              and is less than or equal to <10>"));
    }

    @Nonnull
    protected static CombinedMappingMatcher<Iterable<Integer>, Integer> givenInstance() {
        return new CombinedMappingMatcher<>(MAPPER, MATCHERS, STREAM_MATCHER, "first item");
    }

}
