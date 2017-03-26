package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.CompareTo.isGreaterThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.CompareTo.isLessThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.IterableMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class IterableMatchersUnitTest {

    protected static final Matcher<Integer> MATCHER1 = isGreaterThanOrEqualTo(0);
    protected static final Matcher<Integer> MATCHER2 = isLessThanOrEqualTo(10);

    @Test
    public void factoryMethodContainsOnlyElementsThat() throws Exception {
        final Matcher<Iterable<Integer>> instance = containsOnlyElementsThat(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsOnlyElementsThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    public void factoryMethodContainsOnlyElements() throws Exception {
        final Matcher<Iterable<Integer>> instance = containsOnlyElements(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsOnlyElementsThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    public void factoryMethodContainsAtLeastOneElementThat() throws Exception {
        final Matcher<Iterable<Integer>> instance = containsAtLeastOneElementThat(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsAtLeastOneElementThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

    @Test
    public void factoryMethodContainsAtLeastOneElement() throws Exception {
        final Matcher<Iterable<Integer>> instance = containsAtLeastOneElement(MATCHER1, MATCHER2);

        assertThat(instance, instanceOf(ContainsAtLeastOneElementThat.class));
        assertThat(((CombinedMappingMatcher<?, ?>) instance).matchers(), equalTo(asList(MATCHER1, MATCHER2)));
    }

}
