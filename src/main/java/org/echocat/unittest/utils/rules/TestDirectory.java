package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

import static org.echocat.unittest.utils.nio.Relation.classRelationFor;

public class TestDirectory extends TemporaryDirectoryBasedRuleSupport<TestDirectory> {

    @Nonnull
    protected static final ContentProducer<Path> NOOP_CONTENT_PRODUCER = (relation, to) -> {
    };

    @Nonnull
    private final ContentProducer<Path> contentProducer;
    @Nonnull
    private final String name;

    public TestDirectory() {
        this("test.directory");
    }

    public TestDirectory(@Nonnull String name) {
        this(name, null);
    }

    public TestDirectory(@Nonnull String name, @Nullable ContentProducer<Path> contentProducer) {
        this.name = name;
        this.contentProducer = contentProducer != null ? contentProducer : NOOP_CONTENT_PRODUCER;
    }

    /**
     * @deprecated Use {@link #TestDirectory(String, ContentProducer)} instead.
     */
    @Deprecated
    public TestDirectory(@Nullable ContentProducer<Path> contentProducer) {
        this("test", contentProducer);
    }

    @Override
    protected Path evaluatePath(@Nonnull Statement base, @Nonnull Description description, @Nonnull TemporaryPathBroker broker) throws Throwable {
        return broker.newDirectory(name(), classRelationFor(description.getTestClass()), contentProducer());
    }

    @Nonnull
    protected String name() {
        return name;
    }

    @Nonnull
    protected ContentProducer<Path> contentProducer() {
        return contentProducer;
    }

}
