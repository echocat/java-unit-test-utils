package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.WrappedPath;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

public class TestDirectory extends TemporaryDirectoryBasedRuleSupport<TestDirectory> implements WrappedPath {

    @Nullable
    private final ContentProducer contentProducer;


    public TestDirectory() {
        this(null);
    }

    public TestDirectory(@Nullable ContentProducer contentProducer) {
        this.contentProducer = contentProducer;
    }

    @Override
    protected void evaluate(@Nonnull Statement base, @Nonnull Description description, @Nonnull Path baseDirectory) throws Throwable {
        if (contentProducer != null) {
            contentProducer.produce(baseDirectory);
        }
        base.evaluate();
    }

    @Override
    @Nonnull
    public Path wrapped() {
        return baseDirectory();
    }

    @FunctionalInterface
    public static interface ContentProducer {
        public void produce(@Nonnull Path path) throws Exception;
    }

    @Override
    public String toString() {return wrapped().toString();}

}
