package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.CombinedMappingMatcher.StreamMatcher;
import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

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
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
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
    void factoryMethodCollectMatchers() throws Exception {
        assertThat(collectMatchers(MATCHER_1), equalTo(singletonList(MATCHER_1)));
        assertThat(collectMatchers(MATCHER_1, MATCHER_2), equalTo(asList(MATCHER_1, MATCHER_2)));
    }

    @Test
    void constructor() throws Exception {
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = new CombinedMappingMatcher<>(MAPPER, MATCHERS, STREAM_MATCHER, "first item");

        assertThat(instance.mapper(), sameInstance(MAPPER));
        assertThat(instance.matchers(), sameInstance(MATCHERS));
        assertThat(instance.streamMatcher(), sameInstance(STREAM_MATCHER));
        assertThat(instance.description(), equalTo("first item"));
    }

    @Test
    void matches() throws Exception {
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
    void describeMismatchForNull() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch((Object) null, description);

        assertThat(description.toString(), equalTo("was null"));
    }

    @Test
    void describeMismatchForIllegalType() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(1L, description);

        assertThat(description.toString(), equalTo("was unexpected type <" + Long.class + ">"));
    }

    @Test
    void describeMismatchFor2MatchersAndOneMismatch() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(singleton(666), description);

        assertThat(description.toString(), equalTo("for <666> is less than or equal to <10> was <666>"));
    }

    @Test
    void describeMismatchFor2MatchersAndTwoMismatches() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstanceFor2FailingMatchers();

        instance.describeMismatch(singleton(1), description);

        assertThat(description.toString(), equalTo("for <1> is greater than or equal to <5> was <1>\n" +
            "              and is equal to <10> was <1>"));
    }

    @Test
    void describeMismatchForMoreThan11Mismatches() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(asList(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), description);

        assertThat(description.toString(), equalTo("for <-1> is greater than or equal to <0> was <-1>\n" +
            "          for <-2> is greater than or equal to <0> was <-2>\n" +
            "          for <-3> is greater than or equal to <0> was <-3>\n" +
            "          for <-4> is greater than or equal to <0> was <-4>\n" +
            "          for <-5> is greater than or equal to <0> was <-5>\n" +
            "          for <-6> is greater than or equal to <0> was <-6>\n" +
            "          for <-7> is greater than or equal to <0> was <-7>\n" +
            "          for <-8> is greater than or equal to <0> was <-8>\n" +
            "          for <-9> is greater than or equal to <0> was <-9>\n" +
            "          for <-10> is greater than or equal to <0> was <-10>"));
    }

    @Test
    void describeMismatchForMoreThan10Mismatches() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeMismatch(asList(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12), description);

        assertThat(description.toString(), equalTo("for <-1> is greater than or equal to <0> was <-1>\n" +
            "          for <-2> is greater than or equal to <0> was <-2>\n" +
            "          for <-3> is greater than or equal to <0> was <-3>\n" +
            "          for <-4> is greater than or equal to <0> was <-4>\n" +
            "          for <-5> is greater than or equal to <0> was <-5>\n" +
            "          for <-6> is greater than or equal to <0> was <-6>\n" +
            "          for <-7> is greater than or equal to <0> was <-7>\n" +
            "          for <-8> is greater than or equal to <0> was <-8>\n" +
            "          for <-9> is greater than or equal to <0> was <-9>\n" +
            "          for <-10> is greater than or equal to <0> was <-10>\n" +
            "          [...]"));
    }

    @Test
    void describeToFor2MatchersAndOneFails() throws Exception {
        final Description description = givenDescription();
        final CombinedMappingMatcher<Iterable<Integer>, Integer> instance = givenInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("first item is greater than or equal to <0>\n" +
            "              and is less than or equal to <10>"));
    }

    @Nonnull
    private static CombinedMappingMatcher<Iterable<Integer>, Integer> givenInstance() {
        return new CombinedMappingMatcher<>(MAPPER, MATCHERS, STREAM_MATCHER, "first item");
    }

    @Nonnull
    private static CombinedMappingMatcher<Iterable<Integer>, Integer> givenInstanceFor2FailingMatchers() {
        return new CombinedMappingMatcher<>(MAPPER, asList(isGreaterThanOrEqualTo(5), isEqualTo(10)), STREAM_MATCHER, "first item");
    }

}
