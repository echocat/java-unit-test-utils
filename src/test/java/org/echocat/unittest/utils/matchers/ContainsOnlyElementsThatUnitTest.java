package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.utils.StreamUtils;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static org.echocat.unittest.utils.matchers.ContainsOnlyElementsThat.streamMatcherInstance;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ContainsOnlyElementsThatUnitTest {

    @Nonnull
    protected static final Function<Iterable<Integer>, Stream<Integer>> MAPPER = StreamUtils::toStream;

    @Test
    public void constructor() throws Exception {
        final Set<Matcher<Integer>> matchers = givenMatchers();
        final ContainsOnlyElementsThat<Iterable<Integer>, Integer> instance = new ContainsOnlyElementsThat<>(MAPPER, matchers);

        assertThat(instance.description(), equalTo("contains only elements that"));
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
