package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.utils.ExceptionUtils.Execution;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.utils.ExceptionUtils.executeSafe;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ExceptionUtilsUnitTest {

    @Test
    void executeSafe_works_as_expected() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        final Execution<SpecificException> target3 = givenExecution();

        executeSafe(SpecificException.class, asList(target1, target2, target3));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_still_executes_others_on_specific_Exception_of_at_least_one() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new SpecificException("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(SpecificException.class, "foo"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_still_executes_others_on_Error_of_at_least_one() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new TestError("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(TestError.class, "foo"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_still_executes_others_on_RuntimeException_of_at_least_one() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new RuntimeException("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(RuntimeException.class, "foo"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_still_executes_others_on_IOException_wrapped_in_UncheckedIOException_of_at_least_one() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new IOException("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(UncheckedIOException.class, "foo"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_still_executes_others_on_Exception_wrapped_in_RuntimeException_of_at_least_one() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new Exception("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(RuntimeException.class, "foo"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_will_always_respect_Error_higher_than_Exceptions_order1() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new IOException("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();
        doThrow(new TestError("bar")).when(target3).execute();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(TestError.class, "bar"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    @Test
    void executeSafe_will_always_respect_Error_higher_than_Exceptions_order2() throws Exception {
        final Execution<SpecificException> target1 = givenExecution();
        doThrow(new TestError("bar")).when(target1).execute();
        final Execution<SpecificException> target2 = givenExecution();
        doThrow(new IOException("foo")).when(target2).execute();
        final Execution<SpecificException> target3 = givenExecution();

        assertThat(() -> executeSafe(SpecificException.class, asList(target1, target2, target3)),
            throwsException(TestError.class, "bar"));

        verify(target1, times(1)).execute();
        verify(target2, times(1)).execute();
        verify(target3, times(1)).execute();
    }

    private static Execution<SpecificException> givenExecution() {
        //noinspection unchecked
        return mock(Execution.class);
    }

    private static class SpecificException extends Exception {

        public SpecificException(String message) {
            super(message);
        }

    }

    private static class TestError extends Error {

        public TestError(String message) {
            super(message);
        }

    }


}