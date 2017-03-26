package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.ThrowsException.Execution;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.UndeclaredThrowableException;

import static java.util.regex.Pattern.compile;
import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsExceptionWithMessage;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ThrowsExceptionUnitTest {

    @Test
    public void factoryMethodThrowsException() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class, ".*test");

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    public void factoryMethodThrowsExceptionProvidingNullMessage() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class, (String) null);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    public void factoryMethodThrowsExceptionWithoutPattern() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    public void factoryMethodThrowsExceptionWithMessage() throws Exception {
        final Matcher<Execution> instance = throwsExceptionWithMessage(TestException.class, ".*test");

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    public void factoryMethodThrowsExceptionWithMessageProvidingNullMessage() throws Exception {
        final Matcher<Execution> instance = throwsExceptionWithMessage(TestException.class, (String) null);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    public void constructor() throws Exception {
        final Matcher<Execution> instance = new ThrowsException<>(TestException.class, compile(".*test"));

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    public void matchesProducesNoException() throws Exception {
        final Execution execution = givenNoopExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        assertThat(instance.matches(execution), equalTo(false));
    }

    @Test
    public void matchesProducesExpectedExceptionIgnoringMessage() throws Exception {
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        assertThat(instance.matches(execution), equalTo(true));
    }

    @Test
    public void matchesProducesExpectedExceptionRespectingMessage() throws Exception {
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        assertThat(instance.matches(execution), equalTo(true));
    }

    @Test
    public void matchesProducesUnexpectedError() throws Exception {
        final Execution execution = givenUnexpectedErrorThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        try {
            instance.matches(execution);
            fail("Expected error missing.");
        } catch (final UnexpectedTestError ignored) {
            // Expected
        }
    }

    @Test
    public void matchesProducesUnexpectedRuntimeException() throws Exception {
        final Execution execution = givenUnexpectedRuntimeExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        try {
            instance.matches(execution);
            fail("Expected exception missing.");
        } catch (final UnexpectedTestRuntimeException ignored) {
            // Expected
        }
    }

    @Test
    public void matchesProducesUnexpectedThrowable() throws Exception {
        final Execution execution = givenUnexpectedThrowableThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        try {
            instance.matches(execution);
            fail("Expected throwable missing.");
        } catch (final UndeclaredThrowableException ignored) {
            // Expected
        }
    }

    @Test
    public void describeToWhichIgnoresMessage() throws Exception {
        final Description description = givenDescription();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("execution should throw exception of type <" + TestException.class + ">"));
    }

    @Test
    public void describeToWhichRespectsMessage() throws Exception {
        final Description description = givenDescription();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("execution should throw exception of type <" + TestException.class + "> with message that matches <test>"));
    }

    @Test
    public void describeMismatchForProducesNoException() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenNoopExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("throws no exception"));
    }

    @Test
    public void describeMismatchForMessageIgnoring() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo(""));
    }

    @Test
    public void describeMismatchForNullMessage() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionWithNullMessageThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("message of exception was null"));
    }

    @Test
    public void describeMismatchForOtherMessage() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionWithOtherMessageThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("message of exception was \"other\""));
    }

    @Nonnull
    protected static Matcher<Execution> givenMessageIgnoringInstance() {
        return throwsException(TestException.class);
    }

    @Nonnull
    protected static Matcher<Execution> givenMessageRespectingInstance() {
        return throwsException(TestException.class, "test");
    }

    @Nonnull
    protected static Execution givenNoopExecution() {
        return () -> {
        };
    }

    @Nonnull
    protected static Execution givenExpectedExceptionThrowingExecution() {
        return () -> {
            throw new TestException("test");
        };
    }

    @Nonnull
    protected static Execution givenExpectedExceptionWithNullMessageThrowingExecution() {
        return () -> {
            throw new TestException(null);
        };
    }

    @Nonnull
    protected static Execution givenExpectedExceptionWithOtherMessageThrowingExecution() {
        return () -> {
            throw new TestException("other");
        };
    }

    @Nonnull
    protected static Execution givenUnexpectedErrorThrowingExecution() {
        return () -> {
            throw new UnexpectedTestError();
        };
    }

    @Nonnull
    protected static Execution givenUnexpectedRuntimeExceptionThrowingExecution() {
        return () -> {
            throw new UnexpectedTestRuntimeException();
        };
    }

    @Nonnull
    protected static Execution givenUnexpectedThrowableThrowingExecution() {
        return () -> {
            throw new UnexpectedTestThrowable();
        };
    }

    public static class TestException extends RuntimeException {

        public TestException() {
        }

        public TestException(String message) {
            super(message);
        }
    }

    public static class UnexpectedTestError extends Error {
    }

    public static class UnexpectedTestRuntimeException extends RuntimeException {
    }

    public static class UnexpectedTestThrowable extends Throwable {
    }

}
