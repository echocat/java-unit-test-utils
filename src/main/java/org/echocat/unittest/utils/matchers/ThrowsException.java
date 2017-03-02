package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.ThrowsException.Execution;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Collections.synchronizedMap;

@SuppressWarnings("NonExceptionNameEndsWithException")
public class ThrowsException<T extends Execution> extends TypeSafeMatcher<T> {

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsException(@Nonnull Class<? extends Throwable> type, @Nullable Pattern messagePattern) {
        return new ThrowsException<>(type, messagePattern);
    }

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsException(@Nonnull Class<? extends Throwable> type, @Nullable String messagePattern) {
        return throwsException(type, messagePattern != null ? Pattern.compile(messagePattern, Pattern.DOTALL) : null);
    }

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsException(@Nonnull Class<? extends Throwable> type) {
        return throwsException(type, (Pattern) null);
    }

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsExceptionWithMessage(@Nonnull Class<? extends Throwable> type, @Nullable Pattern messagePattern) {
        return throwsException(type, messagePattern);
    }

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsExceptionWithMessage(@Nonnull Class<? extends Throwable> type, @Nullable String messagePattern) {
        return throwsException(type, messagePattern);
    }

    @Nonnull
    protected static final Throwable NONE = new Throwable() {
    };

    @Nonnull
    private final Map<Execution, Throwable> executionToThrowable = synchronizedMap(new WeakHashMap<>());

    @Nonnull
    private final Class<? extends Throwable> expectedExceptionType;
    @Nullable
    private final Pattern expectedExceptionMessagePattern;

    protected ThrowsException(@Nonnull Class<? extends Throwable> expectedExceptionType, @Nullable Pattern expectedExceptionMessagePattern) {
        super(Execution.class);
        this.expectedExceptionType = expectedExceptionType;
        this.expectedExceptionMessagePattern = expectedExceptionMessagePattern;
    }

    @Override
    protected boolean matchesSafely(@Nonnull T item) {
        final Throwable e = executionToThrowable.computeIfAbsent(item, this::executeExecutionAndReturnException);
        // noinspection ObjectEquality
        if (e == NONE) {
            return false;
        }
        if (expectedExceptionType.isInstance(e)) {
            if (expectedExceptionMessagePattern == null) {
                return true;
            }
            final String message = e.getMessage();
            return message != null && expectedExceptionMessagePattern.matcher(message).matches();
        }
        if (e instanceof Error) {
            throw (Error) e;
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        throw new UndeclaredThrowableException(e);
    }

    @Nonnull
    protected Throwable executeExecutionAndReturnException(@Nonnull Execution execution) {
        try {
            execution.execute();
            return NONE;
        } catch (final Throwable e) {
            return e;
        }
    }

    @Override
    public void describeTo(@Nonnull Description description) {
        description.appendText("exeuction should throw exception of type ").appendValue(expectedExceptionType);
        if (expectedExceptionMessagePattern != null) {
            description.appendText(" with message that machtes ").appendValue(expectedExceptionMessagePattern);
        }
    }

    @Override
    protected void describeMismatchSafely(@Nonnull T item, @Nonnull Description mismatchDescription) {
        final Throwable e = executionToThrowable.get(item);
        if (e == null) {
            mismatchDescription.appendText("execution was not executed");
            return;
        }
        //noinspection ObjectEquality
        if (e == NONE) {
            mismatchDescription.appendText("execution throws no execption");
            return;
        }
        if (!expectedExceptionType.isInstance(e)) {
            mismatchDescription.appendText("throws unexpected exception ").appendValue(e);
            return;
        }
        if (expectedExceptionMessagePattern == null) {
            throw new IllegalStateException("This method should not be called if the exception type is the execpected one but there is no pattern to match the message.");
        }
        final String message = e.getMessage();
        if (message == null) {
            mismatchDescription.appendText("message of exception was ").appendValue(null);
            return;
        }
        mismatchDescription.appendText("message of exception was ").appendText(message);
    }

    @FunctionalInterface
    public interface Execution {
        @SuppressWarnings("ProhibitedExceptionDeclared")
        void execute() throws Throwable;
    }

}
