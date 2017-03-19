package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.rules.TestFile;
import org.junit.ClassRule;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static java.nio.file.Files.*;
import static org.echocat.unittest.utils.rules.TestFile.withGeneratedContent;
import static org.echocat.unittest.utils.utils.IOUtils.copy;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IOUtilsUnitTest {

    private static final long SMALL_FILE_SIZE = IOUtils.BUFFER_SIZE / 10;
    private static final long LARGE_FILE_SIZE = IOUtils.BUFFER_SIZE * 4;

    @ClassRule
    public static final TestFile SMALL_CONTENT_FILE = new TestFile("smallContent.bin", withGeneratedContent(SMALL_FILE_SIZE));
    @ClassRule
    public static final TestFile LARGE_CONTENT_FILE = new TestFile("largeContent.bin", withGeneratedContent(LARGE_FILE_SIZE));

    @Test
    public void constructur() throws Exception {
        new IOUtils();
    }

    @Test
    public void copyWithSmallContent() throws Exception {
        try (final InputStream is = newInputStream(SMALL_CONTENT_FILE)) {
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                final long read = copy(is, os);

                assertThat(read, equalTo(SMALL_FILE_SIZE));
                assertThat(read, equalTo(size(SMALL_CONTENT_FILE)));
                assertThat(os.toByteArray(), equalTo(bytesOf(SMALL_CONTENT_FILE)));
            }
        }
    }

    @Test
    public void copyWithLargeContent() throws Exception {
        try (final InputStream is = newInputStream(LARGE_CONTENT_FILE)) {
            try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                final long read = copy(is, os);

                assertThat(read, equalTo(LARGE_FILE_SIZE));
                assertThat(read, equalTo(size(LARGE_CONTENT_FILE)));
                assertThat(os.toByteArray(), equalTo(bytesOf(LARGE_CONTENT_FILE)));
            }
        }
    }

    @Nonnull
    protected static byte[] bytesOf(@Nonnull Path file) throws IOException {
        return readAllBytes(file);
    }

}
