package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Supplier;

import static java.nio.file.Files.*;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.FileUtils.deleteRecursively;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;

@Immutable
public class TemporaryPathBroker implements AutoCloseable {

    @Nonnull
    public static TemporaryPathBroker temporaryResourceBrokerFor(@Nonnull String baseResourceName) {
        final String name = normalizeName(baseResourceName);
        try {
            final Path baseDirectory = createTempDirectory(name + ".");
            return new TemporaryPathBroker(baseDirectory);
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create base directory for '" + name + "' in system's temporary directory.", e);
        }
    }

    @Nonnull
    private final Path root;
    private final boolean failOnProblemsWhileCleanup;

    protected TemporaryPathBroker(@Nonnull Path root) {
        this(root, true);
    }

    protected TemporaryPathBroker(@Nonnull Path root, boolean failOnProblemsWhileCleanup) {
        this.root = requireNonNull(root, "root");
        this.failOnProblemsWhileCleanup = failOnProblemsWhileCleanup;
    }

    @Nonnull
    public Path newFile(@Nonnull String name, @Nonnull Relation<?> relation) {
        return newFile(name, relation, null);
    }

    @Nonnull
    public Path newFile(@Nonnull String name, @Nonnull Relation<?> relation, @Nullable ContentProducer<OutputStream> producer) {
        final Path result = root().resolve(name);
        try {
            createFile(result);
            if (producer != null) {
                try (final OutputStream os = newOutputStream(result)) {
                    producer.produce(relation, os);
                }
            }
        } catch (final RuntimeException e) {
            throw e;
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create file '" + name + "' under '" + this + "'.", e);
        } catch (final Exception e) {
            throw new RuntimeException("Could not create file '" + name + "' under '" + this + "'.", e);
        }
        return result;
    }

    @Nonnull
    public Path newDirectory(@Nonnull String name, @Nonnull Relation<?> relation) {
        return newDirectory(name, relation, null);
    }

    @Nonnull
    public Path newDirectory(@Nonnull String name, @Nonnull Relation<?> relation, @Nullable ContentProducer<Path> producer) {
        final Path result = root().resolve(name);
        try {
            createDirectory(result);
            if (producer != null) {
                producer.produce(relation, result);
            }
        } catch (final RuntimeException e) {
            throw e;
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create directory '" + name + "' under '" + this + "'.", e);
        } catch (final Exception e) {
            throw new RuntimeException("Could not create directory '" + name + "' under '" + this + "'.", e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        deleteRecursively(root(), failOnProblemsWhileCleanup());
    }

    @Nonnull
    public Path root() {
        return root;
    }

    public boolean failOnProblemsWhileCleanup() {
        return failOnProblemsWhileCleanup;
    }

    @Nonnull
    public TemporaryPathBroker failOnProblemsWhileCleanup(boolean failOnProblemsWhileCleanup) {
        return new TemporaryPathBroker(root(), failOnProblemsWhileCleanup);
    }

    @Override
    public String toString() {
        return this.root().toString();
    }

    @FunctionalInterface
    public interface ContentProducer<T> {

        void produce(@Nonnull Relation<?> relation, @Nonnull T to) throws Exception;

    }

    @FunctionalInterface
    public interface Relation<T> extends Supplier<T> {

        @Nonnull
        @Override
        T get();

        @Nonnull
        static Class<?> typeOf(@Nonnull Relation<?> relation) {
            if (relation instanceof ObjectRelation<?>) {
                return relation.get().getClass();
            }
            if (relation instanceof ClassRelation<?>) {
                return ((ClassRelation<?>) relation).get();
            }
            throw new IllegalArgumentException("Could not handle relation: " + relation);
        }

        @Nullable
        static Object targetOf(@Nonnull Relation<?> relation) {
            if (relation instanceof ObjectRelation<?>) {
                return requireNonNull(relation.get());
            }
            return null;
        }

        @Nonnull
        static <T> ClassRelation<T> classRelationFor(@Nonnull Class<T> type) {
            return () -> type;
        }

        @Nonnull
        static <T> ObjectRelation<T> objectRelationFor(@Nonnull T object) {
            return () -> object;
        }

    }

    @FunctionalInterface
    public interface ObjectRelation<T> extends Relation<T> {}

    @FunctionalInterface
    public interface ClassRelation<T> extends Relation<Class<T>> {}

}
