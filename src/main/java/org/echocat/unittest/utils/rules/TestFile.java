package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.WrappedPath;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.IOUtils.copy;

public class TestFile extends TemporaryDirectoryBasedRuleSupport<TestFile> implements WrappedPath {

    @Nonnull
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Nonnull
    private final String name;
    @Nullable
    private final ContentProducer contentProducer;

    @Nullable
    private Path wrapped;

    public TestFile(@Nonnull String name) {
        this(name, (ContentProducer) null);
    }

    public TestFile(@Nonnull String name, @Nullable String content) {
        this(name, content != null ? content.getBytes(UTF8) : null);
    }

    public TestFile(@Nonnull String name, @Nullable byte[] content) {
        this(name, content != null ? (outputStream -> outputStream.write(content)) : null);
    }

    public TestFile(@Nonnull String name, @Nullable ContentProducer contentProducer) {
        this.name = requireNonNull(name);
        this.contentProducer = contentProducer;
    }

    @Override
    protected void evaluate(@Nonnull Statement base, @Nonnull Description description, @Nonnull Path baseDirectory) throws Throwable {
        this.wrapped = generateFileFor(baseDirectory);
        try {
            base.evaluate();
        } finally {
            this.wrapped = null;
        }
    }

    @Override
    @Nonnull
    public Path wrapped() {
        return requireNonNull(wrapped, "Method was not called within test evaluation or @Rule/@ClassRule was missing at field.");
    }

    @Nonnull
    protected Path generateFileFor(@Nonnull Path baseDirectory) throws Exception {
        final Path result = baseDirectory.resolve(name);
        try (final OutputStream os = Files.newOutputStream(result)) {
            if (contentProducer != null) {
                contentProducer.produce(os);
            }
        }
        return result;
    }

    @FunctionalInterface
    public static interface ContentProducer {
        public void produce(@Nonnull OutputStream os) throws Exception;
    }

    @Override
    public String toString() {return wrapped().toString();}

    @Nonnull
    public static ContentProducer fromClasspath(@Nonnull String name, @Nonnull Class<?> ofClass) {
        return os -> {
            try (final InputStream is = ofClass.getResourceAsStream(name)) {
                copy(is, os);
            }
        };
    }

    @Nonnull
    public static ContentProducer fromClasspath(@Nonnull String path, @Nonnull ClassLoader ofClassLoader) {
        return os -> {
            try (final InputStream is = ofClassLoader.getResourceAsStream(path)) {
                copy(is, os);
            }
        };
    }

    @Nonnull
    public static ContentProducer fromClasspath(@Nonnull String path) {
        return fromClasspath(path, currentThread().getContextClassLoader());
    }

}
