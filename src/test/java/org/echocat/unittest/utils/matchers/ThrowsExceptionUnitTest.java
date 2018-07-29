package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.ThrowsException.Execution;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

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
    void factoryMethodThrowsException() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class, ".*test");

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    void factoryMethodThrowsExceptionProvidingNullMessage() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class, (String) null);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    void factoryMethodThrowsExceptionWithoutPattern() throws Exception {
        final Matcher<Execution> instance = throwsException(TestException.class);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    void factoryMethodThrowsExceptionWithMessage() throws Exception {
        final Matcher<Execution> instance = throwsExceptionWithMessage(TestException.class, ".*test");

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    void factoryMethodThrowsExceptionWithMessageProvidingNullMessage() throws Exception {
        final Matcher<Execution> instance = throwsExceptionWithMessage(TestException.class, (String) null);

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(false));
    }

    @Test
    void constructor() throws Exception {
        final Matcher<Execution> instance = new ThrowsException<>(TestException.class, compile(".*test"));

        assertThat(instance, instanceOf(ThrowsException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionType(), sameInstance(TestException.class));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().isPresent(), equalTo(true));
        assertThat(((ThrowsException<?>) instance).expectedExceptionMessagePattern().get().toString(), equalTo(".*test"));
    }

    @Test
    void matchesProducesNoException() throws Exception {
        final Execution execution = givenNoopExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        assertThat(instance.matches(execution), equalTo(false));
    }

    @Test
    void matchesProducesExpectedExceptionIgnoringMessage() throws Exception {
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        assertThat(instance.matches(execution), equalTo(true));
    }

    @Test
    void matchesProducesExpectedExceptionRespectingMessage() throws Exception {
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        assertThat(instance.matches(execution), equalTo(true));
    }

    @Test
    void matchesProducesUnexpectedError() throws Exception {
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
    void matchesProducesUnexpectedRuntimeException() throws Exception {
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
    void matchesProducesUnexpectedThrowable() throws Exception {
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
    void describeToWhichIgnoresMessage() throws Exception {
        final Description description = givenDescription();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("execution should throw exception of type <" + TestException.class + ">"));
    }

    @Test
    void describeToWhichRespectsMessage() throws Exception {
        final Description description = givenDescription();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("execution should throw exception of type <" + TestException.class + "> with message that matches <test>"));
    }

    @Test
    void describeMismatchForProducesNoException() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenNoopExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("throws no exception"));
    }

    @Test
    void describeMismatchForMessageIgnoring() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionThrowingExecution();
        final Matcher<Execution> instance = givenMessageIgnoringInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo(""));
    }

    @Test
    void describeMismatchForNullMessage() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionWithNullMessageThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("message of exception was null"));
    }

    @Test
    void describeMismatchForOtherMessage() throws Exception {
        final Description description = givenDescription();
        final Execution execution = givenExpectedExceptionWithOtherMessageThrowingExecution();
        final Matcher<Execution> instance = givenMessageRespectingInstance();

        instance.describeMismatch(execution, description);

        assertThat(description.toString(), equalTo("message of exception was \"other\""));
    }

    @Nonnull
    private static Matcher<Execution> givenMessageIgnoringInstance() {
        return throwsException(TestException.class);
    }

    @Nonnull
    private static Matcher<Execution> givenMessageRespectingInstance() {
        return throwsException(TestException.class, "test");
    }

    @Nonnull
    private static Execution givenNoopExecution() {
        return () -> {
        };
    }

    @Nonnull
    private static Execution givenExpectedExceptionThrowingExecution() {
        return () -> {
            throw new TestException("test");
        };
    }

    @Nonnull
    private static Execution givenExpectedExceptionWithNullMessageThrowingExecution() {
        return () -> {
            throw new TestException(null);
        };
    }

    @Nonnull
    private static Execution givenExpectedExceptionWithOtherMessageThrowingExecution() {
        return () -> {
            throw new TestException("other");
        };
    }

    @Nonnull
    private static Execution givenUnexpectedErrorThrowingExecution() {
        return () -> {
            throw new UnexpectedTestError();
        };
    }

    @Nonnull
    private static Execution givenUnexpectedRuntimeExceptionThrowingExecution() {
        return () -> {
            throw new UnexpectedTestRuntimeException();
        };
    }

    @Nonnull
    private static Execution givenUnexpectedThrowableThrowingExecution() {
        return () -> {
            throw new UnexpectedTestThrowable();
        };
    }

    private static class TestException extends RuntimeException {

        public TestException() {
        }

        public TestException(String message) {
            super(message);
        }
    }

    private static class UnexpectedTestError extends Error {
    }

    private static class UnexpectedTestRuntimeException extends RuntimeException {
    }

    private static class UnexpectedTestThrowable extends Throwable {
    }

}
