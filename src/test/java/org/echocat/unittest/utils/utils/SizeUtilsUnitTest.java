package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.rules.TestDirectory;
import org.echocat.unittest.utils.rules.TestFile;
import org.junit.ClassRule;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import static org.echocat.unittest.utils.TestUtils.*;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.utils.SizeUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SizeUtilsUnitTest {

    @ClassRule
    public static final TestFile FILE_OF_LENGTH_4 = new TestFile("fileOfLength4.txt", "0123");
    @ClassRule
    public static final TestFile EMPTY_FILE = new TestFile("emptyFile.txt", "");
    @ClassRule
    public static final TestDirectory EMPTY_DIRECTORY = new TestDirectory();

    @Test
    public void constructur() throws Exception {
        new SizeUtils();
    }

    @Test
    public void sizeOfNull() throws Exception {
        assertThat(sizeOf(null), equalTo(0L));
    }

    @Test
    public void sizeOfStreamWithLength4() throws Exception {
        final Object toTest = givenStreamWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyStream() throws Exception {
        final Object toTest = givenEmptyStream();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfCollectionWithLength4() throws Exception {
        final Object toTest = givenCollectionOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyCollection() throws Exception {
        final Object toTest = givenEmptyCollection();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfIterableWithLength4() throws Exception {
        final Object toTest = givenIterableOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfIterableCollection() throws Exception {
        final Object toTest = givenEmptyIterable();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfIteratorWithLength4() throws Exception {
        final Object toTest = givenIteratorOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyIterator() throws Exception {
        final Object toTest = givenEmptyIterator();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfMapWithLength4() throws Exception {
        final Object toTest = givenMapWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyMap() throws Exception {
        final Object toTest = givenEmptyMap();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfArrayWithLength4() throws Exception {
        final Object toTest = givenArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyArray() throws Exception {
        final Object toTest = givenEmptyArray();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfPathWithLength4() throws Exception {
        final Object toTest = givenPathWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyPath() throws Exception {
        final Object toTest = givenEmptyPath();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfNotExistingPathFails() throws Exception {
        final Object toTest = givenNotExistingPath();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void sizeOfFileWithLength4() throws Exception {
        final Object toTest = givenFileWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyFile() throws Exception {
        final Object toTest = givenEmptyFile();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfNotExistingFileFails() throws Exception {
        final Object toTest = givenNotExistingFile();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void sizeOfInputStreamWithLength4() throws Exception {
        final Object toTest = givenInputStreamWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyInputStream() throws Exception {
        final Object toTest = givenEmptyInputStream();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfThrowsExceptionWhenProblemWhileReadFromInputStream() throws Exception {
        final Object toTest = givenExceptionThrowingInputStream();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.IOException: test"));
    }

    @Test
    public void sizeOfReaderWithLength4() throws Exception {
        final Object toTest = givenReaderWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyReader() throws Exception {
        final Object toTest = givenEmptyReader();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfThrowsExceptionWhenProblemWhileReadFromReader() throws Exception {
        final Object toTest = givenExceptionThrowingReader();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.IOException: test"));
    }

    @Test
    public void sizeOfURLWithLength4() throws Exception {
        final Object toTest = givenURLWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyURL() throws Exception {
        final Object toTest = givenEmptyURL();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfThrowsExceptionWhenProblemWhileReadFromURL() throws Exception {
        final Object toTest = givenExceptionThrowingURL();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.FileNotFoundException: notExisting.*"));
    }

    @Test
    public void sizeOfURIWithLength4() throws Exception {
        final Object toTest = givenURIWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyURI() throws Exception {
        final Object toTest = givenEmptyURI();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfThrowsExceptionWhenProblemWhileReadFromURI() throws Exception {
        final Object toTest = givenExceptionThrowingURI();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.net.MalformedURLException: unknown protocol: notexisting"));
    }

    @Test
    public void sizeOfUnknownTypeThrowsException() throws Exception {
        assertThat(() -> sizeOf(1), throwsException(IllegalArgumentException.class, "Could not get size of java.lang.Integer."));
    }

    @Test
    public void isEmptyOfNull() throws Exception {
        assertThat(isEmpty(null), equalTo(true));
    }

    @Test
    public void isEmptyOfStreamWithLength4() throws Exception {
        final Object toTest = givenStreamWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyStream() throws Exception {
        final Object toTest = givenEmptyStream();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfCollectionWithLength4() throws Exception {
        final Object toTest = givenCollectionOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyCollection() throws Exception {
        final Object toTest = givenEmptyCollection();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfIterableWithLength4() throws Exception {
        final Object toTest = givenIterableOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfIterableCollection() throws Exception {
        final Object toTest = givenEmptyIterable();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfIteratorWithLength4() throws Exception {
        final Object toTest = givenIteratorOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyIterator() throws Exception {
        final Object toTest = givenEmptyIterator();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfMapWithLength4() throws Exception {
        final Object toTest = givenMapWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyMap() throws Exception {
        final Object toTest = givenEmptyMap();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfArrayWithLength4() throws Exception {
        final Object toTest = givenArrayWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyArray() throws Exception {
        final Object toTest = givenEmptyArray();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfPathWithLength4() throws Exception {
        final Object toTest = givenPathWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyPath() throws Exception {
        final Object toTest = givenEmptyPath();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfUnknownPathThrowsException() throws Exception {
        final Object toTest = givenNotExistingPath();

        assertThat(() -> isEmpty(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void isEmptyOfFileWithLength4() throws Exception {
        final Object toTest = givenFileWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyFile() throws Exception {
        final Object toTest = givenFileWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfUnknownFileThrowsException() throws Exception {
        final Object toTest = givenNotExistingFile();

        assertThat(() -> isEmpty(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void isEmptyOfUnknownTypeThrowsException() throws Exception {
        assertThat(() -> isEmpty(1), throwsException(IllegalArgumentException.class, "Could not get size of java.lang.Integer."));
    }

    @Test
    public void isEmptyInputStreamWithLength4() throws Exception {
        final Object toTest = givenInputStreamWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyEmptyInputStream() throws Exception {
        final Object toTest = givenEmptyInputStream();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyReaderWithLength4() throws Exception {
        final Object toTest = givenReaderWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyEmptyReader() throws Exception {
        final Object toTest = givenEmptyReader();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyURLWithLength4() throws Exception {
        final Object toTest = givenURLWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyEmptyURL() throws Exception {
        final Object toTest = givenEmptyURL();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyURIWithLength4() throws Exception {
        final Object toTest = givenURIWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyEmptyURI() throws Exception {
        final Object toTest = givenEmptyURI();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isNotEmptyOfNull() throws Exception {
        assertThat(isNotEmpty(null), equalTo(false));
    }

    @Test
    public void isNotEmptyOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(isNotEmpty(toTest), equalTo(true));
    }

    @Test
    public void isNotEmptyOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(isNotEmpty(toTest), equalTo(false));
    }

    @Nonnull
    protected static Path givenPathWithLength4() {
        return FILE_OF_LENGTH_4;
    }

    @Nonnull
    protected static Path givenEmptyPath() {
        return EMPTY_FILE;
    }

    @Nonnull
    protected static Path givenNotExistingPath() {
        return EMPTY_DIRECTORY.resolve("doesNotExist");
    }

    @Nonnull
    protected static File givenFileWithLength4() {
        return givenPathWithLength4().toFile();
    }

    @Nonnull
    protected static File givenEmptyFile() {
        return givenEmptyPath().toFile();
    }

    @Nonnull
    protected static File givenNotExistingFile() {
        return givenNotExistingPath().toFile();
    }


}
