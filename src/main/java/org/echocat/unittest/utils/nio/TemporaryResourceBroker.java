package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.utils.ThrowableCombiner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.*;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.FileUtils.deleteRecursively;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;

public class TemporaryResourceBroker implements Closeable {

    @Nonnull
    public static TemporaryResourceBroker temporaryResourceBrokerFor(@Nonnull String baseResourceName) {
        final String name = normalizeName(baseResourceName);
        try {
            final Path baseDirectory = createTempDirectory(name + "-");
            return new TemporaryResourceBroker(baseDirectory);
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create base directory for '" + name + "' in system's temporary directory.", e);
        }
    }

    @Nonnull
    private final Path baseDirectory;
    @Nonnull
    private final List<TemporaryResourceBroker> children = new LinkedList<>();

    private volatile boolean failOnProblemsWhileCleanup = true;

    protected TemporaryResourceBroker(@Nonnull Path baseDirectory) {
        this.baseDirectory = requireNonNull(baseDirectory, "baseDirectory");
    }

    @Nonnull
    public TemporaryResourceBroker newChildFor(@Nonnull String baseResourceName) {
        final String name = normalizeName(baseResourceName);
        try {
            final Path baseDirectory = baseDirectory().resolve(name);
            createDirectory(baseDirectory);
            final TemporaryResourceBroker child = new TemporaryResourceBroker(baseDirectory);
            synchronized (children) {
                children.add(child);
            }
            return child;
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create base directory for '" + name + "' under '" + this + "'.", e);
        }
    }

    @Nonnull
    public TemporaryResourceBroker newChild() {
        return newChildFor(UUID.randomUUID().toString());
    }

    @Nonnull
    public Path newFile(@Nonnull String name, @Nonnull Object relation) {
        return newFile(name, relation,null);
    }

    @Nonnull
    public Path newFile(@Nonnull String name, @Nonnull Object relation, @Nullable ContentProducer<OutputStream> producer) {
        final Path result = baseDirectory().resolve(name);
        try {
            createFile(result);
            if (producer != null) {
                try (final OutputStream os = Files.newOutputStream(result)) {
                    producer.produce(relation, os);
                }
            }
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create file '" + name + "' under '" + this + "'.", e);
        }
        return result;
    }

    @Nonnull
    public Path newDirectory(@Nonnull String name, @Nonnull Object relation) {
        return newDirectory(name, relation,null);
    }

    @Nonnull
    public Path newDirectory(@Nonnull String name, @Nonnull Object relation, @Nullable ContentProducer<Path> producer) {
        final Path result = baseDirectory().resolve(name);
        try {
            createDirectory(result);
            if (producer != null) {
                producer.produce(relation, result);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not create directory '" + name + "' under '" + this + "'.", e);
        }
        return result;
    }

    @Override
    public void close() {
        final ThrowableCombiner combiner = new ThrowableCombiner();
        synchronized (children) {
            for (final TemporaryResourceBroker child: children) {
                try {
                    child.close();
                } catch (final Throwable e) {
                    combiner.add(e);
                }
            }
            children.clear();
        }
        try {
            deleteRecursively(baseDirectory(), failOnProblemsWhileCleanup());
        } catch (final Throwable e) {
            combiner.add(e);
        }
        combiner.throwUncheckedIfAny();
    }

    @Nonnull
    protected Path baseDirectory() {
        return baseDirectory;
    }

    public boolean failOnProblemsWhileCleanup() {
        return failOnProblemsWhileCleanup;
    }

    public TemporaryResourceBroker failOnProblemsWhileCleanup(boolean failOnProblemsWhileCleanup) {
        this.failOnProblemsWhileCleanup = failOnProblemsWhileCleanup;
        return this;
    }

    @Override
    public String toString() {
        return this.baseDirectory().toString();
    }

    @FunctionalInterface
    public static interface ContentProducer<T> {

        void produce(@Nonnull Object relation, @Nonnull T to) throws IOException;

    }

}
