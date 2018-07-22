package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.WrappedEvent.Interceptor;
import org.echocat.unittest.utils.nio.WrappedPath;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.temporaryPathBrokerFor;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;

public abstract class TemporaryDirectoryBasedRuleSupport<T extends TemporaryDirectoryBasedRuleSupport<T>> implements TestRule, WrappedPath {

    @Nonnull
    private Optional<Path> path = empty();
    @Nonnull
    private Optional<Interceptor> interceptor = empty();
    private boolean failOnProblemsWhileCleanup = true;

    public boolean isFailOnProblemsWhileCleanup() {
        return failOnProblemsWhileCleanup;
    }

    @Nonnull
    public T setFailOnProblemsWhileCleanup(boolean failOnProblemsWhileCleanup) {
        this.failOnProblemsWhileCleanup = failOnProblemsWhileCleanup;
        //noinspection unchecked
        return (T) this;
    }

    @Nonnull
    @Override
    public Optional<Interceptor> interceptor() {
        return interceptor;
    }

    @Nonnull
    public T setInterceptor(@Nullable Interceptor interceptor) {
        this.interceptor = ofNullable(interceptor);
        //noinspection unchecked
        return (T) this;
    }

    @Override
    public Statement apply(@Nonnull Statement base, @Nonnull Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try (final TemporaryPathBroker broker = newBrokerFor(description)
                    .failOnProblemsWhileCleanup(isFailOnProblemsWhileCleanup())
                ) {
                    path = of(evaluatePath(base, description, broker));
                    try {
                        base.evaluate();
                    } finally {
                        path = empty();
                    }
                }
            }
        };
    }

    @Nonnull
    protected TemporaryPathBroker newBrokerFor(@Nonnull Description description) {
        return temporaryResourceBrokerFor(description);
    }

    protected abstract Path evaluatePath(@Nonnull Statement base, @Nonnull Description description, @Nonnull TemporaryPathBroker broker) throws Throwable;

    @Nonnull
    @Override
    public Path wrapped() {
        return path.orElseThrow(() -> new IllegalStateException("Method was not called within test evaluation or @Rule/@ClassRule was missing at field."));
    }

    @Nonnull
    protected TemporaryPathBroker temporaryResourceBrokerFor(@Nonnull Description description) {
        return temporaryPathBrokerFor(normalizeName(description.getDisplayName()));
    }

    @Override
    public String toString() {return wrapped().toString();}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof WrappedPath) {
            return wrapped().equals(((WrappedPath) o).wrapped());
        }
        if (o instanceof Path) {
            return wrapped().equals(o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return wrapped().hashCode();
    }

    @Override
    public int compareTo(Path other) {
        if (other instanceof WrappedPath) {
            return wrapped().compareTo(((WrappedPath) other).wrapped());
        }
        return wrapped().compareTo(other);
    }
}
