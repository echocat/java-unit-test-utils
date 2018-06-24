package org.echocat.unittest.utils.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

import static java.nio.file.Files.createTempDirectory;
import static java.util.Objects.requireNonNull;
import static org.echocat.unittest.utils.utils.FileUtils.deleteRecursively;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;

public abstract class TemporaryDirectoryBasedRuleSupport<T extends TemporaryDirectoryBasedRuleSupport<T>> implements TestRule {

    @Nullable
    private Path baseDirectory;

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

    @Override
    public Statement apply(@Nonnull Statement base, @Nonnull Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                TemporaryDirectoryBasedRuleSupport.this.evaluate(base, description);
            }
        };
    }

    protected void evaluate(@Nonnull Statement base, @Nonnull Description description) throws Throwable {
        final Path baseDirectory = generateTemporaryFolderFor(description);
        TemporaryDirectoryBasedRuleSupport.this.baseDirectory = baseDirectory;
        try {
            evaluate(base, description, baseDirectory);
        } finally {
            TemporaryDirectoryBasedRuleSupport.this.baseDirectory = null;
            deleteRecursively(baseDirectory, isFailOnProblemsWhileCleanup());
        }
    }

    protected abstract void evaluate(@Nonnull Statement base, @Nonnull Description description, @Nonnull Path baseDirectory) throws Throwable;

    @Nonnull
    protected Path baseDirectory() {
        return requireNonNull(baseDirectory, "Method was not called within test evaluation or @Rule/@ClassRule was missing at field.");
    }

    @Nonnull
    protected Path generateTemporaryFolderFor(@Nonnull Description description) throws Exception {
        final String name = folderNameFor(description);
        return createTempDirectory(name + "-");
    }

    @Nonnull
    protected String folderNameFor(@Nonnull Description description) {
        return normalizeName(description.getDisplayName());
    }

}
