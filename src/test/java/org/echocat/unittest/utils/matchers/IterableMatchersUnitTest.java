package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.CompareTo.isGreaterThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.CompareTo.isLessThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.IterableMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
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


    @Test
    public void factoryMethodStartsWith() throws Exception {
        final Matcher<List<String>> instance = startsWith("a", "b", "c");

        assertThat(instance, instanceOf(IterableBasedMatcher.class));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparator(), sameInstance(IterableBasedMatcher.startsWithComparator()));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparatorDescription(), equalTo("starts with"));
        assertThat(((IterableBasedMatcher<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void factoryMethodEndsWith() throws Exception {
        final Matcher<List<String>> instance = endsWith("a", "b", "c");

        assertThat(instance, instanceOf(IterableBasedMatcher.class));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparator(), sameInstance(IterableBasedMatcher.endsWithComparator()));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparatorDescription(), equalTo("ends with"));
        assertThat(((IterableBasedMatcher<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @Test
    public void factoryMethodContains() throws Exception {
        final Matcher<List<String>> instance = IterableMatchers.contains("a", "b", "c");

        assertThat(instance, instanceOf(IterableBasedMatcher.class));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparator(), sameInstance(IterableBasedMatcher.containsComparator()));
        assertThat(((IterableBasedMatcher<?, ?>) instance).comparatorDescription(), equalTo("contains"));
        assertThat(((IterableBasedMatcher<?, ?>) instance).expected(), equalTo(iterableOf("a", "b", "c")));
    }

    @SafeVarargs
    @Nonnull
    protected static <T> Iterable<T> iterableOf(@Nonnull T... values) {
        return asList(values);
    }

    @Test
    public void constructor() {
        new IterableMatchers();
    }

}
