package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static org.echocat.unittest.utils.matchers.ContainsAtLeastOneElementThat.streamMatcherInstance;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ContainsAtLeastOneElementThatUnitTest {

    @Nonnull
    protected static final Function<Iterable<Integer>, Stream<Integer>> MAPPER = StreamUtils::toStream;

    @Test
    public void constructor() throws Exception {
        final Set<Matcher<Integer>> matchers = givenMatchers();
        final ContainsAtLeastOneElementThat<Iterable<Integer>, Integer> instance = new ContainsAtLeastOneElementThat<>(MAPPER, matchers);

        assertThat(instance.description(), equalTo("contains at least one element that"));
        assertThat(instance.mapper(), sameInstance(MAPPER));
        assertThat(instance.matchers(), sameInstance(matchers));
        assertThat(instance.streamMatcher(), sameInstance(streamMatcherInstance()));
    }

    @Test
    public void matches() throws Exception {
    }

    @Nonnull
    protected static Set<Matcher<Integer>> givenMatchers() {
        return singleton(isEqualTo(0));
    }

}
