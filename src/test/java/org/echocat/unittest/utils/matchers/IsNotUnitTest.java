package org.echocat.unittest.utils.matchers;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.matchers.IsNot.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class IsNotUnitTest {

    @Test
    void factoryMethodNot() throws Exception {
        final Matcher<Object> enclosed = givenMatcher();
        final Matcher<Object> instance = not(enclosed);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), sameInstance(enclosed));
    }

    @Test
    void factoryMethodIsNot() throws Exception {
        final Matcher<Object> enclosed = givenMatcher();
        final Matcher<Object> instance = isNot(enclosed);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), sameInstance(enclosed));
    }

    @Test
    void factoryMethodNot2() throws Exception {
        final Object expected = givenAnObject();
        final Matcher<Object> instance = not(expected);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) ((IsNot<?>) instance).enclosed()).expected(), sameInstance(expected));
    }

    @Test
    void factoryMethodIsNot2() throws Exception {
        final Object expected = givenAnObject();
        final Matcher<Object> instance = isNot(expected);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) ((IsNot<?>) instance).enclosed()).expected(), sameInstance(expected));
    }

    @Test
    void factoryMethodNotEqualTo() throws Exception {
        final Object expected = givenAnObject();
        final Matcher<Object> instance = notEqualTo(expected);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) ((IsNot<?>) instance).enclosed()).expected(), sameInstance(expected));
    }

    @Test
    void factoryMethodIsNotEqualTo() throws Exception {
        final Object expected = givenAnObject();
        final Matcher<Object> instance = isNotEqualTo(expected);

        assertThat(instance, instanceOf(IsNot.class));
        assertThat(((IsNot<?>) instance).enclosed(), instanceOf(IsEqualTo.class));
        assertThat(((IsEqualTo<?>) ((IsNot<?>) instance).enclosed()).expected(), sameInstance(expected));
    }

    @Test
    void constructor() throws Exception {
        final Matcher<Object> enclosed = givenMatcher();
        final IsNot<Object> instance = new IsNot<>(enclosed);

        assertThat(instance.enclosed(), sameInstance(enclosed));
    }

    @Nonnull
    private static Matcher<Object> givenMatcher() {
        //noinspection unchecked
        return mock(Matcher.class);
    }

    @Nonnull
    private static Object givenAnObject() {
        return new Object();
    }

}
