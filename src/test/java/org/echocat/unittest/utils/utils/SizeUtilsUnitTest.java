package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.extensions.TemporaryDirectory;
import org.echocat.unittest.utils.extensions.TemporaryFile;
import org.echocat.unittest.utils.extensions.TemporaryPaths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static org.echocat.unittest.utils.TestUtils.*;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.utils.SizeUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@ExtendWith(TemporaryPaths.class)
public class SizeUtilsUnitTest {

    @Test
    void constructur() throws Exception {
        new SizeUtils();
    }

    @Test
    void sizeOfNull() throws Exception {
        assertThat(sizeOf(null), equalTo(0L));
    }

    @Test
    void sizeOfStreamWithLength4() throws Exception {
        final Object toTest = givenStreamWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyStream() throws Exception {
        final Object toTest = givenEmptyStream();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfCollectionWithLength4() throws Exception {
        final Object toTest = givenCollectionOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyCollection() throws Exception {
        final Object toTest = givenEmptyCollection();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfIterableWithLength4() throws Exception {
        final Object toTest = givenIterableOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfIterableCollection() throws Exception {
        final Object toTest = givenEmptyIterable();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfIteratorWithLength4() throws Exception {
        final Object toTest = givenIteratorOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyIterator() throws Exception {
        final Object toTest = givenEmptyIterator();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfMapWithLength4() throws Exception {
        final Object toTest = givenMapWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyMap() throws Exception {
        final Object toTest = givenEmptyMap();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfArrayWithLength4() throws Exception {
        final Object toTest = givenArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfBooleanArrayWithLength4() throws Exception {
        final Object toTest = givenBooleanArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfByteArrayWithLength4() throws Exception {
        final Object toTest = givenByteArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfCharArrayWithLength4() throws Exception {
        final Object toTest = givenCharArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfShortArrayWithLength4() throws Exception {
        final Object toTest = givenShortArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfIntArrayWithLength4() throws Exception {
        final Object toTest = givenIntArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfLongArrayWithLength4() throws Exception {
        final Object toTest = givenLongArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfFloatArrayWithLength4() throws Exception {
        final Object toTest = givenFloatArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfDoubleArrayWithLength4() throws Exception {
        final Object toTest = givenDoubleArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyArray() throws Exception {
        final Object toTest = givenEmptyArray();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfPathWithLength4(@TemporaryFile(withRandomContentOfLength = 4) Path pathWithLengthOf4) throws Exception {
        assertThat(sizeOf(pathWithLengthOf4), equalTo(4L));
    }

    @Test
    void sizeOfEmptyPath(@TemporaryFile Path emptyPath) throws Exception {
        assertThat(sizeOf(emptyPath), equalTo(0L));
    }

    @Test
    void sizeOfNotExistingPathFails(@TemporaryDirectory Path root) throws Exception {
        final Object toTest = givenNotExistingPath(root);

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    void sizeOfFileWithLength4(@TemporaryFile(withRandomContentOfLength = 4) Path pathWithLengthOf4) throws Exception {
        final Object toTest = pathWithLengthOf4.toFile();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyFile(@TemporaryFile Path emptyPath) throws Exception {
        final Object toTest = emptyPath.toFile();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfNotExistingFileFails(@TemporaryDirectory Path root) throws Exception {
        final Object toTest = givenNotExistingPath(root).toFile();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    void sizeOfInputStreamWithLength4() throws Exception {
        final Object toTest = givenInputStreamWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyInputStream() throws Exception {
        final Object toTest = givenEmptyInputStream();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfThrowsExceptionWhenProblemWhileReadFromInputStream() throws Exception {
        final Object toTest = givenExceptionThrowingInputStream();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.IOException: test"));
    }

    @Test
    void sizeOfReaderWithLength4() throws Exception {
        final Object toTest = givenReaderWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyReader() throws Exception {
        final Object toTest = givenEmptyReader();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfThrowsExceptionWhenProblemWhileReadFromReader() throws Exception {
        final Object toTest = givenExceptionThrowingReader();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.IOException: test"));
    }

    @Test
    void sizeOfURLWithLength4() throws Exception {
        final Object toTest = givenURLWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyURL() throws Exception {
        final Object toTest = givenEmptyURL();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfThrowsExceptionWhenProblemWhileReadFromURL() throws Exception {
        final Object toTest = givenExceptionThrowingURL();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.io.FileNotFoundException: notExisting.*"));
    }

    @Test
    void sizeOfURIWithLength4() throws Exception {
        final Object toTest = givenURIWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    void sizeOfEmptyURI() throws Exception {
        final Object toTest = givenEmptyURI();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    void sizeOfThrowsExceptionWhenProblemWhileReadFromURI() throws Exception {
        final Object toTest = givenExceptionThrowingURI();

        assertThat(() -> sizeOf(toTest), throwsException(UncheckedIOException.class, "java.net.MalformedURLException: unknown protocol: notexisting"));
    }

    @Test
    void sizeOfUnknownTypeThrowsException() throws Exception {
        assertThat(() -> sizeOf(1), throwsException(IllegalArgumentException.class, "Could not get size of java.lang.Integer."));
    }

    @Test
    void isEmptyOfNull() throws Exception {
        assertThat(isEmpty(null), equalTo(true));
    }

    @Test
    void isEmptyOfStreamWithLength4() throws Exception {
        final Object toTest = givenStreamWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyStream() throws Exception {
        final Object toTest = givenEmptyStream();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfCollectionWithLength4() throws Exception {
        final Object toTest = givenCollectionOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyCollection() throws Exception {
        final Object toTest = givenEmptyCollection();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfIterableWithLength4() throws Exception {
        final Object toTest = givenIterableOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfIterableCollection() throws Exception {
        final Object toTest = givenEmptyIterable();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfIteratorWithLength4() throws Exception {
        final Object toTest = givenIteratorOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyIterator() throws Exception {
        final Object toTest = givenEmptyIterator();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfMapWithLength4() throws Exception {
        final Object toTest = givenMapWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyMap() throws Exception {
        final Object toTest = givenEmptyMap();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfArrayWithLength4() throws Exception {
        final Object toTest = givenArrayWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyArray() throws Exception {
        final Object toTest = givenEmptyArray();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyOfPathWithLength4(@TemporaryFile(withRandomContentOfLength = 4) Path pathWithLengthOf4) throws Exception {
        assertThat(isEmpty(pathWithLengthOf4), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyPath(@TemporaryFile Path empty) throws Exception {
        assertThat(isEmpty(empty), equalTo(true));
    }

    @Test
    void isEmptyOfUnknownPathThrowsException(@TemporaryDirectory Path root) throws Exception {
        final Object toTest = givenNotExistingPath(root);

        assertThat(() -> isEmpty(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    void isEmptyOfFileWithLength4(@TemporaryFile(withRandomContentOfLength = 4) Path pathWithLengthOf4) throws Exception {
        final Object toTest = pathWithLengthOf4.toFile();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfEmptyFile(@TemporaryFile(withRandomContentOfLength = 4) Path pathWithLengthOf4) throws Exception {
        final Object toTest = pathWithLengthOf4.toFile();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyOfUnknownFileThrowsException(@TemporaryDirectory Path root) throws Exception {
        final Object toTest = givenNotExistingPath(root).toFile();

        assertThat(() -> isEmpty(toTest), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    void isEmptyOfUnknownTypeThrowsException() throws Exception {
        assertThat(() -> isEmpty(1), throwsException(IllegalArgumentException.class, "Could not get size of java.lang.Integer."));
    }

    @Test
    void isEmptyInputStreamWithLength4() throws Exception {
        final Object toTest = givenInputStreamWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyEmptyInputStream() throws Exception {
        final Object toTest = givenEmptyInputStream();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyReaderWithLength4() throws Exception {
        final Object toTest = givenReaderWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyEmptyReader() throws Exception {
        final Object toTest = givenEmptyReader();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyURLWithLength4() throws Exception {
        final Object toTest = givenURLWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyEmptyURL() throws Exception {
        final Object toTest = givenEmptyURL();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isEmptyURIWithLength4() throws Exception {
        final Object toTest = givenURIWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    void isEmptyEmptyURI() throws Exception {
        final Object toTest = givenEmptyURI();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    void isNotEmptyOfNull() throws Exception {
        assertThat(isNotEmpty(null), equalTo(false));
    }

    @Test
    void isNotEmptyOfCharSequenceWithLength4() throws Exception {
        final Object toTest = givenCharSequenceWithLength4();

        assertThat(isNotEmpty(toTest), equalTo(true));
    }

    @Test
    void isNotEmptyOfEmptyCharSequence() throws Exception {
        final Object toTest = givenEmptyCharSequence();

        assertThat(isNotEmpty(toTest), equalTo(false));
    }

    @Nonnull
    private static Path givenNotExistingPath(@Nonnull Path root) {
        return root.resolve("doesNotExist");
    }

}
