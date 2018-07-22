package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.extensions.TemporaryDirectory;
import org.echocat.unittest.utils.extensions.TemporaryPaths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isRegularFile;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.utils.FileUtils.deleteRecursively;
import static org.echocat.unittest.utils.utils.FileUtils.normalizeName;
import static org.junit.Assert.assertThat;

@ExtendWith(TemporaryPaths.class)
class FileUtilsUnitTest {

    @Test
    void normalizeName_preserve_allowed_characters() throws Exception {
        final String actual = normalizeName("abcdefghijklmnopqrstuvwxyz0123456789.,-_@~()[]!ä");

        assertThat(actual, isEqualTo("abcdefghijklmnopqrstuvwxyz0123456789.,-_@~()[]!ä"));
    }

    @Test
    void normalizeName_replaces_forbidden_characters() throws Exception {
        final String actual = normalizeName("/\\*&|?:;$%");

        assertThat(actual, isEqualTo("__________"));
    }

    @Test
    void deleteRecursively_works_with_files(@TemporaryDirectory Path root) throws Exception {
        final Path file = root.resolve("file.ext");
        createFile(file);
        assertThat(isRegularFile(file), isEqualTo(true));

        deleteRecursively(file);

        assertThat(exists(file), isEqualTo(false));
    }

    @Test
    void deleteRecursively_works_with_non_existing_paths(@TemporaryDirectory Path root) throws Exception {
        final Path sub = root.resolve("foobar");
        assertThat(exists(sub), isEqualTo(false));

        deleteRecursively(sub);

        assertThat(exists(sub), isEqualTo(false));
    }

    @Test
    void deleteRecursively_works_with_directories(@TemporaryDirectory Path root) throws Exception {
        final Path directory = root.resolve("foo");
        final Path file = directory.resolve("bar/file.ext");
        createDirectories(file.getParent());
        createFile(file);
        assertThat(isRegularFile(file), isEqualTo(true));

        deleteRecursively(directory);

        assertThat(exists(directory), isEqualTo(false));
    }

}