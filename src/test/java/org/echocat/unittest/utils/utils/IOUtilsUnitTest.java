package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.extensions.TemporaryFile;
import org.echocat.unittest.utils.extensions.TemporaryPaths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static java.nio.file.Files.*;
import static org.echocat.unittest.utils.utils.IOUtils.copy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@ExtendWith(TemporaryPaths.class)
public class IOUtilsUnitTest {

    private static final long SMALL_FILE_SIZE = IOUtils.BUFFER_SIZE / 10;
    private static final long LARGE_FILE_SIZE = IOUtils.BUFFER_SIZE * 4;

    @Test
    void constructur() throws Exception {
        new IOUtils();
    }

    @Test
    void copyWithSmallContent(@TemporaryFile(withRandomContentOfLength = SMALL_FILE_SIZE) Path file) throws Exception {
        try (final InputStream is = newInputStream(file)) {
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                final long read = copy(is, os);

                assertThat(read, equalTo(SMALL_FILE_SIZE));
                assertThat(read, equalTo(size(file)));
                assertThat(os.toByteArray(), equalTo(bytesOf(file)));
            }
        }
    }

    @Test
    void copyWithLargeContent(@TemporaryFile(withRandomContentOfLength = LARGE_FILE_SIZE) Path file) throws Exception {
        try (final InputStream is = newInputStream(file)) {
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                final long read = copy(is, os);

                assertThat(read, equalTo(LARGE_FILE_SIZE));
                assertThat(read, equalTo(size(file)));
                assertThat(os.toByteArray(), equalTo(bytesOf(file)));
            }
        }
    }

    @Nonnull
    private static byte[] bytesOf(@Nonnull Path file) throws IOException {
        return readAllBytes(file);
    }

}
