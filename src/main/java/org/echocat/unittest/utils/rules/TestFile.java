package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Random;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.utils.IOUtils.copy;

public class TestFile extends TemporaryDirectoryBasedRuleSupport<TestFile> {

    @Nonnull
    private static final Random RANDOM = new Random();

    @Nonnull
    private final String name;
    @Nullable
    private final ContentProducer<OutputStream> contentProducer;

    public TestFile(@Nonnull String name) {
        this(name, (ContentProducer<OutputStream>) null);
    }

    /**
     * @deprecated Use {{@link #TestFile(String, ContentProducer)}} with {{@link #withContent(String)}} instead.
     */
    @Deprecated
    public TestFile(@Nonnull String name, @Nullable String content) {
        this(name, content != null ? withContent(content) : null);
    }

    /**
     * @deprecated Use {{@link #TestFile(String, ContentProducer)}} with {{@link #withContent(String)}} instead.
     */
    @Deprecated
    public TestFile(@Nonnull String name, @Nullable byte[] content) {
        this(name, content != null ? withContent(content) : null);
    }

    public TestFile(@Nonnull String name, @Nullable ContentProducer<OutputStream> contentProducer) {
        this.name = requireNonNull(name);
        this.contentProducer = contentProducer;
    }

    @Override
    protected Path evaluatePath(@Nonnull Statement base, @Nonnull Description description, @Nonnull TemporaryPathBroker broker) throws Throwable {
        return broker.newFile(name, classRelationFor(description.getTestClass()), contentProducer);
    }

    @Nonnull
    public static ContentProducer<OutputStream> withContent(@Nonnull byte[] content) {
        return (relation, os) -> os.write(content);
    }

    @Nonnull
    public static ContentProducer<OutputStream> withContent(@Nonnull String content) {
        return withContent(content.getBytes(UTF_8));
    }

    @Nonnull
    public static ContentProducer<OutputStream> withGeneratedContent(@Nonnegative long numberOfBytes) {
        return (relation, os) -> {
            final byte[] buf = new byte[4096];
            long written = 0L;
            while (written < numberOfBytes) {
                RANDOM.nextBytes(buf);
                final long rest = numberOfBytes - written;
                final long toWrite = buf.length > rest ? rest : buf.length;
                os.write(buf, 0, (int) toWrite);
                written += toWrite;
            }
        };
    }

    @Nonnull
    public static ContentProducer<OutputStream> fromClasspath(@Nonnull String name, @Nonnull Class<?> ofClass) {
        return (relation, os) -> {
            try (final InputStream is = ofClass.getResourceAsStream(name)) {
                copy(is, os);
            }
        };
    }

    @Nonnull
    public static ContentProducer<OutputStream> fromClasspath(@Nonnull String path, @Nonnull ClassLoader ofClassLoader) {
        return (relation, os) -> {
            try (final InputStream is = ofClassLoader.getResourceAsStream(path)) {
                copy(is, os);
            }
        };
    }

    @Nonnull
    public static ContentProducer<OutputStream> fromClasspath(@Nonnull String path) {
        return fromClasspath(path, currentThread().getContextClassLoader());
    }

}
