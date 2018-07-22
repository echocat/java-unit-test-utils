package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.WrappedEvent.Interceptor;
import org.echocat.unittest.utils.nio.WrappedEvent.InterceptorEnabled;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.echocat.unittest.utils.nio.WrappedEvent.eventOf;

public class WrappedExecution {

    public static <T, W extends Wrapping<T>, R> R withResult(
        @Nonnull W wrapping,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull ExecutionWithResult<T, R, RuntimeException> execution,
        @Nonnull Object... arguments
    ) {
        return withResult(wrapping, eventType, RuntimeException.class, execution, arguments);
    }

    public static <T, W extends Wrapping<T>, R, E extends Throwable> R withResult(
        @Nonnull W wrapping,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull Class<E> throwing,
        @Nonnull ExecutionWithResult<T, R, E> execution,
        @Nonnull Object... arguments
    ) throws E {
        if (wrapping instanceof InterceptorEnabled) {
            final Optional<Interceptor> interceptor = ((InterceptorEnabled) wrapping).interceptor();
            if (interceptor.isPresent()) {
                return execute(interceptor.get(), eventType, wrapping.wrapped(), throwing, execution, arguments);
            }
        }
        return execution.execute(wrapping.wrapped());
    }

    public static <T, W extends Wrapping<T>> void withoutResult(
        @Nonnull W wrapping,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull ExecutionWithoutResult<T, RuntimeException> execution,
        @Nonnull Object... arguments
    ) {
        withoutResult(wrapping, eventType, RuntimeException.class, execution, arguments);
    }

    public static <T, W extends Wrapping<T>, E extends Throwable> void withoutResult(
        @Nonnull W wrapping,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull Class<E> throwing,
        @Nonnull ExecutionWithoutResult<T, E> execution,
        @Nonnull Object... arguments
    ) throws E {
        if (wrapping instanceof InterceptorEnabled) {
            final Optional<Interceptor> interceptor = ((InterceptorEnabled) wrapping).interceptor();
            if (interceptor.isPresent()) {
                execute(interceptor.get(), eventType, wrapping.wrapped(), throwing, execution, arguments);
                return;
            }
        }
        execution.execute(wrapping.wrapped());
    }

    private static <T, R, E extends Throwable> R execute(
        @Nonnull Interceptor interceptor,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull T target,
        @Nonnull Class<E> throwing,
        @Nonnull ExecutionWithResult<T, R, E> execution,
        @Nonnull Object... arguments
    ) throws E {
        final WrappedEvent event = eventOf(eventType, arguments);
        final Optional<Object> before = executeSafe(() -> interceptor.before(event), throwing);
        if (before.isPresent()) {
            //noinspection unchecked
            return (R) before.get();
        }

        final R result = execution.execute(target);

        //noinspection unchecked
        return executeSafe(() -> interceptor.after(event, result), throwing)
            .map(o -> (R) o)
            .orElse(result);
    }

    private static <T, E extends Throwable, W> void execute(
        @Nonnull Interceptor interceptor,
        @Nonnull WrappedEvent.Type eventType,
        @Nonnull T target,
        @Nonnull Class<E> throwing,
        @Nonnull ExecutionWithoutResult<T, E> execution,
        @Nonnull Object... arguments
    ) throws E {
        final WrappedEvent event = eventOf(eventType, arguments);
        final Optional<Object> before = executeSafe(() -> interceptor.before(event), throwing);
        if (before.isPresent()) {
            return;
        }

        execution.execute(target);

        executeSafe(() -> interceptor.after(event, null), throwing);
    }

    private static <E extends Throwable> Optional<Object> executeSafe(@Nonnull Callable<Optional<Object>> callable, @Nonnull Class<E> throwing) throws E {
        try {
            return callable.call();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            if (throwing.isInstance(e)) {
                throw throwing.cast(e);
            }
            if (e instanceof IOException) {
                throw new UncheckedIOException(e.getMessage(), (IOException) e);
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface ExecutionWithResult<T, R, E extends Throwable> {

        R execute(T target) throws E;

    }

    @FunctionalInterface
    public interface ExecutionWithoutResult<T, E extends Throwable> {

        void execute(T target) throws E;

    }

}
