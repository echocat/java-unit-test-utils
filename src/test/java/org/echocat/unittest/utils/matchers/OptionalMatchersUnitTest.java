package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isAbsentBaseChecker;
import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isPresentBaseChecker;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isAbsent;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class OptionalMatchersUnitTest {

    @Test
    void factoryMethodIsPresent() throws Exception {
        final Matcher<Optional<Integer>> instance = isPresent();

        assertThat(instance, instanceOf(OptionalBasedMatcher.class));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseChecker(), sameInstance(isPresentBaseChecker()));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseDescription(), equalTo("is present"));
        assertThat(((OptionalBasedMatcher<Integer>) instance).contentMatcher().isPresent(), equalTo(false));
    }

    @Test
    void factoryMethodIsAbsent() throws Exception {
        final Matcher<Optional<Integer>> instance = isAbsent();

        assertThat(instance, instanceOf(OptionalBasedMatcher.class));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseChecker(), sameInstance(isAbsentBaseChecker()));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseDescription(), equalTo("is absent"));
        assertThat(((OptionalBasedMatcher<Integer>) instance).contentMatcher().isPresent(), equalTo(false));
    }

    @Test
    void factoryMethodWhereContentMatches() throws Exception {
        final Matcher<Integer> contentMatcher = equalTo(666);
        final Matcher<Optional<Integer>> instance = OptionalMatchers.whereContentMatches(contentMatcher);

        assertThat(instance, instanceOf(OptionalBasedMatcher.class));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseChecker(), sameInstance(isPresentBaseChecker()));
        assertThat(((OptionalBasedMatcher<Integer>) instance).baseDescription(), equalTo("is present and content "));
        assertThat(((OptionalBasedMatcher<Integer>) instance).contentMatcher().isPresent(), equalTo(true));
        assertThat(((OptionalBasedMatcher<Integer>) instance).contentMatcher().get(), sameInstance(contentMatcher));
    }

    @Test
    void constructor() throws Exception {
        new OptionalMatchers();
    }

}
