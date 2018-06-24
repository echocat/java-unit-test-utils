package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.lang.Character.isLetterOrDigit;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.*;

public class FileUtils {

    @Nonnull
    public static String normalizeName(@Nonnull String input) {
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

    public static void deleteRecursively(@Nonnull Path what) {
        deleteRecursively(what, true);
    }

    public static void deleteRecursively(@Nonnull Path what, boolean respectExceptions) {
        if (!exists(what)) {
            return;
        }
        if (!isDirectory(what)) {
            try {
                delete(what);
            } catch (final IOException e) {
                handleExceptionIfRequired(e, respectExceptions);
            }
            return;
        }
        try {
            walkFileTree(what, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        delete(file);
                    } catch (final IOException e) {
                        handleExceptionIfRequired(e, respectExceptions);
                    }
                    return CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        handleExceptionIfRequired(exc, respectExceptions);
                    } else {
                        try {
                            delete(dir);
                        } catch (final IOException e) {
                            handleExceptionIfRequired(e, respectExceptions);
                        }
                    }
                    return CONTINUE;
                }
            });
        } catch (final IOException e) {
            handleExceptionIfRequired(e, respectExceptions);
        }
    }

    protected static void handleExceptionIfRequired(@Nonnull IOException e, boolean respectExceptions) {
        if (e instanceof NoSuchFileException) {
            return;
        }
        if (respectExceptions) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }


}
