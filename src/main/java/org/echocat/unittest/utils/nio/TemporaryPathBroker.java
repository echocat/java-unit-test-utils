package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.Files.newOutputStream;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.FileUtils.deleteRecursively;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;

@Immutable
public class TemporaryPathBroker implements AutoCloseable {

    @Nonnull
    public static TemporaryPathBroker temporaryPathBrokerFor(@Nonnull String baseResourceName) {
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
    public Path newFile(@Nonnull String name, @Nonnull Relation<?> relation, @Nonnull ContentProducer<OutputStream> producer) {
        final Path result = root().resolve(name);
        try {
            createDirectories(result.getParent());
            try (final OutputStream os = newOutputStream(result)) {
                producer.produce(relation, os);
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
    public Path newDirectory(@Nonnull String name, @Nonnull Relation<?> relation, @Nonnull ContentProducer<Path> producer) {
        final Path result = root().resolve(name);
        try {
            createDirectories(result);
            producer.produce(relation, result);
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

}
