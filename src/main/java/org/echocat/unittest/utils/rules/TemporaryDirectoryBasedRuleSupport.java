package org.echocat.unittest.utils.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.lang.Character.isLetterOrDigit;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.*;
import static java.util.Objects.requireNonNull;

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
            deleteDirectory(baseDirectory);
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
        return normalizeFolderName(description.getDisplayName());
    }

    @Nonnull
    protected String normalizeFolderName(@Nonnull String input) {
        final char[] inputCharacters = input.toCharArray();
        final char[] result = new char[inputCharacters.length];
        for (int i = 0; i < inputCharacters.length; i++) {
            final char c = inputCharacters[i];
            if (isLetterOrDigit(c)
                || c == '.'
                || c == ','
                || c == '_'
                || c == '-'
                || c == '@'
                || c == '%'
                || c == '~'
                || c == '('
                || c == ')'
                || c == '['
                || c == ']'
                || c == '!'
                ) {
                result[i] = c;
            } else {
                result[i] = '_';
            }
        }
        return new String(result);
    }

    protected void deleteDirectory(@Nonnull Path what) throws Exception {
        try {
            walkFileTree(what, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        delete(file);
                    } catch (final IOException e) {
                        handleExceptionIfRequired(e);
                    }
                    return CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        handleExceptionIfRequired(exc);
                    } else {
                        try {
                            delete(dir);
                        } catch (final IOException e) {
                            handleExceptionIfRequired(e);
                        }
                    }
                    return CONTINUE;
                }
            });
        } catch (final IOException e) {
            handleExceptionIfRequired(e);
        }
    }

    protected void handleExceptionIfRequired(@Nonnull IOException e) throws IOException {
        if (isFailOnProblemsWhileCleanup()) {
            throw e;
        }
    }

}
