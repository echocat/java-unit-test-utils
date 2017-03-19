package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.AppliesTo.appliesTo;
import static org.echocat.unittest.utils.matchers.AppliesTo.apply;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AppliesToUnitTest {

    @Test
    public void factoryMethodAppliesTo() throws Exception {
        final Predicate<Void> predicate = givenSomePredicate();

        final Matcher<Void> matcher = appliesTo(predicate);

        assertThat(matcher, instanceOf(AppliesTo.class));
        assertThat(((AppliesTo<?>) matcher).predicate(), sameInstance(predicate));
    }

    @Test
    public void factoryMethodApply() throws Exception {
        final Predicate<Void> predicate = givenSomePredicate();

        final Matcher<Void> matcher = apply(predicate);

        assertThat(matcher, instanceOf(AppliesTo.class));
        assertThat(((AppliesTo<?>) matcher).predicate(), sameInstance(predicate));
    }

    @Test
    public void constructor() throws Exception {
        final Predicate<Void> predicate = givenSomePredicate();

        final Matcher<Void> matcher = new AppliesTo<>(predicate);

        assertThat(matcher, instanceOf(AppliesTo.class));
        assertThat(((AppliesTo<?>) matcher).predicate(), sameInstance(predicate));
    }

    @Test
    public void matches() throws Exception {
        final Predicate<Integer> predicate = givenTrueAtNumberOnePredicate();
        final AppliesTo<Integer> instance = givenInstanceFor(predicate);

        assertThat(instance.matches(0), equalTo(false));
        assertThat(instance.matches(1), equalTo(true));
    }

    @Test
    public void describeTo() throws Exception {
        final Predicate<Void> predicate = givenSomePredicate();
        final AppliesTo<Void> instance = givenInstanceFor(predicate);
        final Description description = givenDescription();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("applies to <somePredicate>"));
    }

    @Nonnull
    protected static <T> AppliesTo<T> givenInstanceFor(@Nonnull Predicate<T> predicate) {
        return new AppliesTo<>(predicate);
    }

    @Nonnull
    protected static Predicate<Integer> givenTrueAtNumberOnePredicate() {
        return what -> what != null && what == 1;
    }

    @Nonnull
    protected static Predicate<Void> givenSomePredicate() {
        //noinspection unchecked
        final Predicate<Void> predicate = mock(Predicate.class);
        doReturn("somePredicate").when(predicate).toString();
        return predicate;
    }

}
