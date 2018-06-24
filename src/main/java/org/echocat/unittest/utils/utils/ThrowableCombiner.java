package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.of;

public class ThrowableCombiner {

    @Nonnull
    private final AtomicReference<Throwable> top = new AtomicReference<>();

    @Nonnull
    public ThrowableCombiner add(@Nullable Throwable throwable) {
        top.accumulateAndGet(throwable, this::select);
        return this;
    }

    @Nonnull
    public ThrowableCombiner executeAndRecordIfAny(@Nonnull Action action) {
        try {
            action.action();
        } catch (final Throwable e) {
            add(e);
        }
        return this;
    }

    @Nullable
    public <T> T executeAndRecordIfAny(@Nonnull ActionWithResult<T> action) {
        try {
            return action.action();
        } catch (final Throwable e) {
            add(e);
            return null;
        }
    }

    @Nonnull
    public Optional<Throwable> find() {
        return of(top.get());
    }

    public void throwUncheckedIfAny() {
        final Throwable throwable = top.get();
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof IOException) {
            throw new UncheckedIOException((IOException) throwable);
        }
        throw new RuntimeException(throwable);
    }

    public void throwCheckedIfAny() throws Exception {
        final Throwable throwable = top.get();
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof Exception) {
            throw (Exception) throwable;
        }
        throw new Exception(throwable);
    }

    @Nullable
    protected Throwable select(@Nullable Throwable prev, @Nullable Throwable next) {
        if (prev == null && next == null) {
            return null;
        }
        if (prev == null) {
            return next;
        }
        if (next == null) {
            return prev;
        }
        if (next instanceof Error && !(prev instanceof Error)) {
            next.addSuppressed(prev);
            return next;
        }
        prev.addSuppressed(next);
        return prev;
    }

    @FunctionalInterface
    public static interface Action {

        void action() throws Throwable;

    }

    @FunctionalInterface
    public static interface ActionWithResult<T> {

        @Nullable
        T action() throws Throwable;

    }

}
