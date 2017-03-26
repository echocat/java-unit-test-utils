package org.echocat.unittest.utils.matchers;

import org.echocat.unittest.utils.matchers.ThrowsException.Execution;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import static java.util.Collections.synchronizedMap;
import static java.util.regex.Pattern.compile;

@SuppressWarnings("NonExceptionNameEndsWithException")
public class ThrowsException<T extends Execution> extends TypeSafeMatcher<T> {

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsException(@Nonnull Class<? extends Throwable> type, @Nullable Pattern messagePattern) {
        return new ThrowsException<>(type, messagePattern);
    }

    @Nonnull
    public static <T extends Execution> Matcher<T> throwsException(@Nonnull Class<? extends Throwable> type, @Nullable String messagePattern) {
        return throwsException(type, messagePattern != null ? compile(messagePattern, Pattern.DOTALL) : null);
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
        return throwsExceptionWithMessage(type, messagePattern != null ? compile(messagePattern) : null);
    }

    @Nonnull
    protected static final Throwable NONE = new Throwable() {
    };

    @Nonnull
    private final Map<Execution, Throwable> executionToThrowable = synchronizedMap(new WeakHashMap<>());

    @Nonnull
    private final Class<? extends Throwable> expectedExceptionType;
    @Nonnull
    private final Optional<Pattern> expectedExceptionMessagePattern;

    protected ThrowsException(@Nonnull Class<? extends Throwable> expectedExceptionType, @Nullable Pattern expectedExceptionMessagePattern) {
        super(Execution.class);
        this.expectedExceptionType = expectedExceptionType;
        this.expectedExceptionMessagePattern = Optional.ofNullable(expectedExceptionMessagePattern);
    }

    @Override
    protected boolean matchesSafely(@Nonnull T item) {
        final Throwable e = executionToThrowable.computeIfAbsent(item, this::executeExecutionAndReturnException);
        // noinspection ObjectEquality
        if (e == NONE) {
            return false;
        }
        if (expectedExceptionType.isInstance(e)) {
            return expectedExceptionMessagePattern().map(pattern -> {
                final String message = e.getMessage();
                return message != null && pattern.matcher(message).matches();
            }).orElse(true);
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
        description.appendText("execution should throw exception of type ").appendValue(expectedExceptionType);
        expectedExceptionMessagePattern().ifPresent(pattern -> description.appendText(" with message that matches ").appendValue(pattern));
    }

    @Override
    protected void describeMismatchSafely(@Nonnull T item, @Nonnull Description mismatchDescription) {
        final Throwable e = executionToThrowable.computeIfAbsent(item, this::executeExecutionAndReturnException);
        //noinspection ObjectEquality
        if (e == NONE) {
            mismatchDescription.appendText("throws no exception");
            return;
        }
        expectedExceptionMessagePattern()
            .ifPresent(pattern -> {
                final String message = e.getMessage();
                if (message == null) {
                    mismatchDescription.appendText("message of exception was null");
                    return;
                }
                mismatchDescription.appendText("message of exception was ").appendValue(message);
            });
    }

    @Nonnull
    protected Class<? extends Throwable> expectedExceptionType() {
        return expectedExceptionType;
    }

    @Nonnull
    protected Optional<Pattern> expectedExceptionMessagePattern() {
        return expectedExceptionMessagePattern;
    }

    @FunctionalInterface
    public interface Execution {
        void execute() throws Throwable;
    }

}
