package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.CompareTo.isGreaterThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.CompareTo.isLessThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.StreamMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class StreamMatchersUnitTest {

    private static final Matcher<Integer> MATCHER1 = isGreaterThanOrEqualTo(0);
    private static final Matcher<Integer> MATCHER2 = isLessThanOrEqualTo(10);

    @Test
    void factoryMethodContainsOnlyElementsThat() throws Exception {
        final Matcher<Stream<Integer>> instance = containsOnlyElementsThat(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsOnlyElementsThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    void factoryMethodContainsOnlyElements() throws Exception {
        final Matcher<Stream<Integer>> instance = containsOnlyElements(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsOnlyElementsThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    void factoryMethodContainsAtLeastOneElementThat() throws Exception {
        final Matcher<Stream<Integer>> instance = containsAtLeastOneElementThat(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsAtLeastOneElementThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    void factoryMethodContainsAtLeastOneElement() throws Exception {
        final Matcher<Stream<Integer>> instance = containsAtLeastOneElement(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsAtLeastOneElementThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    void constructor() {
        new StreamMatchers();
    }

}
