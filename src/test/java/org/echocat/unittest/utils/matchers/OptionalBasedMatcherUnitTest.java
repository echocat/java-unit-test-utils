package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.OptionalBasedMatcher.BaseChecker;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isAbsentBaseChecker;
import static org.echocat.unittest.utils.matchers.OptionalBasedMatcher.isPresentBaseChecker;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class OptionalBasedMatcherUnitTest {

    @Test
    void factoryMethodIsPresentBaseChecker() throws Exception {
        final BaseChecker<Integer> instance = isPresentBaseChecker();

        assertThat(instance.check(of(666)), equalTo(true));
        assertThat(instance.check(empty()), equalTo(false));
    }

    @Test
    void factoryMethodIsAbsentBaseChecker() throws Exception {
        final BaseChecker<Integer> instance = isAbsentBaseChecker();

        assertThat(instance.check(of(666)), equalTo(false));
        assertThat(instance.check(empty()), equalTo(true));
    }

    @Test
    void constructor() throws Exception {
        final BaseChecker<Integer> baseChecker = isPresentBaseChecker();
        final Matcher<Integer> contentMatcher = equalTo(666);
        final OptionalBasedMatcher<Integer> instance = new OptionalBasedMatcher<>("description", baseChecker, contentMatcher);

        assertThat(instance.baseDescription(), equalTo("description"));
        assertThat(instance.baseChecker(), sameInstance(baseChecker));
        assertThat(instance.contentMatcher().isPresent(), equalTo(true));
        assertThat(instance.contentMatcher().get(), sameInstance(contentMatcher));
    }

    @Test
    void matchesContentEqualsTo() throws Exception {
        final Matcher<Optional<Integer>> instance = givenContentEqualsTo666Instance();

        assertThat(instance.matches(of(666)), equalTo(true));
        assertThat(instance.matches(null), equalTo(false));
        assertThat(instance.matches(666), equalTo(false));
        assertThat(instance.matches(empty()), equalTo(false));
        assertThat(instance.matches(of(1)), equalTo(false));
    }

    @Test
    void matchesOnlyIsPresent() throws Exception {
        final Matcher<Optional<Integer>> instance = givenIsPresentOnlyInstance();

        assertThat(instance.matches(of(666)), equalTo(true));
        assertThat(instance.matches(null), equalTo(false));
        assertThat(instance.matches(666), equalTo(false));
        assertThat(instance.matches(empty()), equalTo(false));
        assertThat(instance.matches(of(1)), equalTo(true));
    }

    @Test
    void matchesOnlyIsAbsent() throws Exception {
        final Matcher<Optional<Integer>> instance = givenIsAbsentOnlyInstance();

        assertThat(instance.matches(of(666)), equalTo(false));
        assertThat(instance.matches(null), equalTo(false));
        assertThat(instance.matches(666), equalTo(false));
        assertThat(instance.matches(empty()), equalTo(true));
        assertThat(instance.matches(of(1)), equalTo(false));
    }

    @Test
    void matchesIsAbsentWithContentChecker() throws Exception {
        final Matcher<Optional<Integer>> instance = givenIsAbsentWithContentChecker();

        assertThat(() -> instance.matches(empty()), throwsException(IllegalStateException.class, "The base checker should cover presence of optional value before."));
    }

    @Test
    void describeToWithContentCheck() throws Exception {
        final Description description = givenDescription();
        final Matcher<Optional<Integer>> instance = givenContentEqualsTo666Instance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("is present and <666>"));
    }

    @Test
    void describeToWithoutContentCheck() throws Exception {
        final Description description = givenDescription();
        final Matcher<Optional<Integer>> instance = givenIsPresentOnlyInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("is present"));
    }

    @Nonnull
    private static Matcher<Optional<Integer>> givenContentEqualsTo666Instance() {
        final BaseChecker<Integer> baseChecker = isPresentBaseChecker();
        final Matcher<Integer> contentMatcher = equalTo(666);
        return new OptionalBasedMatcher<>("is present and ", baseChecker, contentMatcher);
    }

    @Nonnull
    private static Matcher<Optional<Integer>> givenIsPresentOnlyInstance() {
        final BaseChecker<Integer> baseChecker = isPresentBaseChecker();
        return new OptionalBasedMatcher<>("is present", baseChecker, null);
    }

    @Nonnull
    private static Matcher<Optional<Integer>> givenIsAbsentOnlyInstance() {
        final BaseChecker<Integer> baseChecker = isAbsentBaseChecker();
        return new OptionalBasedMatcher<>("is absent", baseChecker, null);
    }

    @Nonnull
    private static Matcher<Optional<Integer>> givenIsAbsentWithContentChecker() {
        final BaseChecker<Integer> baseChecker = isAbsentBaseChecker();
        final Matcher<Integer> contentMatcher = equalTo(666);
        return new OptionalBasedMatcher<>("is absent and ", baseChecker, contentMatcher);
    }

}
