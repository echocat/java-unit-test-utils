package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.stream.Stream;

public final class ExceptionUtils {

    public static <E extends Exception> void executeSafe(@Nonnull Class<E> baseExceptionType, @Nonnull Stream<Execution<? extends E>> executions) throws E {
        executeSafe(baseExceptionType, executions.iterator());
    }

    public static <E extends Exception> void executeSafe(@Nonnull Class<E> baseExceptionType, @Nonnull Iterator<Execution<? extends E>> executions) throws E {
        Throwable throwable = null;
        while (executions.hasNext()) {
            try {
                executions.next().execute();
            } catch (final Throwable e) {
                if (throwable == null) {
                    throwable = e;
                } else if (e instanceof Error && !(throwable instanceof Error)) {
                    e.addSuppressed(throwable);
                    throwable = e;
                } else {
                    throwable.addSuppressed(e);
                }
            }
        }
        if (throwable != null) {
            if (throwable instanceof Error) {
                throw (Error) throwable;
            }
            if (baseExceptionType.isInstance(throwable)) {
                throw baseExceptionType.cast(throwable);
            }
            if (throwable instanceof IOException) {
                throw new UncheckedIOException((IOException) throwable);
            }
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable.getMessage(), throwable);
        }
    }

    public interface Execution<E extends Exception> {
        void execute() throws E;
    }

}
