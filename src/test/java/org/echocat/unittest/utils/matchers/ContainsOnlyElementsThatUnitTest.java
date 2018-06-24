package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static org.echocat.unittest.utils.matchers.ContainsOnlyElementsThat.matches;
import static org.echocat.unittest.utils.matchers.ContainsOnlyElementsThat.streamMatcherInstance;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ContainsOnlyElementsThatUnitTest {

    @Nonnull
    private static final Function<Iterable<Integer>, Stream<Integer>> MAPPER = StreamUtils::toStream;

    @Test
    void constructor() throws Exception {
        final Set<Matcher<Integer>> matchers = givenMatchers();
        final ContainsOnlyElementsThat<Iterable<Integer>, Integer> instance = new ContainsOnlyElementsThat<>(MAPPER, matchers);

        assertThat(instance.description(), equalTo("contains only elements that"));
        assertThat(instance.mapper(), sameInstance(MAPPER));
        assertThat(instance.matchers(), sameInstance(matchers));
        assertThat(instance.streamMatcher(), sameInstance(streamMatcherInstance()));
    }

    @Test
    void matchesWorks() throws Exception {
        final Set<Matcher<Integer>> matchers = givenMatchers();

        assertThat(matches(matchers, Stream.of(0, 1, 2)), equalTo(false));
        assertThat(matches(matchers, Stream.of(-1, 0, 1)), equalTo(false));
        assertThat(matches(matchers, Stream.of(-2, -1, 0)), equalTo(false));
        assertThat(matches(matchers, Stream.of(0)), equalTo(true));
        assertThat(matches(matchers, Stream.of(0, 0, 0, 0)), equalTo(true));
        assertThat(matches(matchers, Stream.of()), equalTo(false));
        assertThat(matches(matchers, Stream.of(1, 2, 3)), equalTo(false));
        assertThat(matches(matchers, Stream.of(1)), equalTo(false));
    }

    @Nonnull
    private static Set<Matcher<Integer>> givenMatchers() {
        return singleton(isEqualTo(0));
    }

}
