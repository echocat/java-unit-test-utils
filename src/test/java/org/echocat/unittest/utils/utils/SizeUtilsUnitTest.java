package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.rules.TestDirectory;
import org.echocat.unittest.utils.rules.TestFile;
import org.junit.ClassRule;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static org.echocat.unittest.utils.TestUtils.*;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.utils.SizeUtils.isEmpty;
import static org.echocat.unittest.utils.utils.SizeUtils.sizeOf;
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
    public void sizeOfNull() throws Exception {
        assertThat(sizeOf(null), equalTo(0L));
    }

    @Test
    public void sizeOfStreamWithLength4() throws Exception {
        final Stream<Integer> toTest = givenStreamWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyStream() throws Exception {
        final Stream<Integer> toTest = givenEmptyStream();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfCollectionWithLength4() throws Exception {
        final Collection<Integer> toTest = givenCollectionOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyCollection() throws Exception {
        final Collection<Integer> toTest = givenEmptyCollection();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfIterableWithLength4() throws Exception {
        final Iterable<Integer> toTest = givenIterableOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfIterableCollection() throws Exception {
        final Iterable<Integer> toTest = givenEmptyIterable();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfIteratorWithLength4() throws Exception {
        final Iterator<Integer> toTest = givenIteratorOfLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyIterator() throws Exception {
        final Iterator<Integer> toTest = givenEmptyIterator();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfMapWithLength4() throws Exception {
        final Map<Integer, Boolean> toTest = givenMapWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyMap() throws Exception {
        final Map<Integer, Boolean> toTest = givenEmptyMap();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfArrayWithLength4() throws Exception {
        final Integer[] toTest = givenArrayWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyArray() throws Exception {
        final Integer[] toTest = givenEmptyArray();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfCharSequenceWithLength4() throws Exception {
        final CharSequence toTest = givenCharSequenceWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyCharSequence() throws Exception {
        final CharSequence toTest = givenEmptyCharSequence();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfPathWithLength4() throws Exception {
        final Path toTest = givenPathWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyPath() throws Exception {
        final Path toTest = givenEmptyPath();

        assertThat(sizeOf(toTest), equalTo(0L));
    }

    @Test
    public void sizeOfFileWithLength4() throws Exception {
        final File toTest = givenFileWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
    }

    @Test
    public void sizeOfEmptyFile() throws Exception {
        assertThat(() -> sizeOf(EMPTY_DIRECTORY.resolve("doesNotExist")), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void sizeOfUnknownFileThrowsException() throws Exception {
        final File toTest = givenFileWithLength4();

        assertThat(sizeOf(toTest), equalTo(4L));
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
        final Stream<Integer> toTest = givenStreamWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyStream() throws Exception {
        final Stream<Integer> toTest = givenEmptyStream();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfCollectionWithLength4() throws Exception {
        final Collection<Integer> toTest = givenCollectionOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyCollection() throws Exception {
        final Collection<Integer> toTest = givenEmptyCollection();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfIterableWithLength4() throws Exception {
        final Iterable<Integer> toTest = givenIterableOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfIterableCollection() throws Exception {
        final Iterable<Integer> toTest = givenEmptyIterable();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfIteratorWithLength4() throws Exception {
        final Iterator<Integer> toTest = givenIteratorOfLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyIterator() throws Exception {
        final Iterator<Integer> toTest = givenEmptyIterator();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfMapWithLength4() throws Exception {
        final Map<Integer, Boolean> toTest = givenMapWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyMap() throws Exception {
        final Map<Integer, Boolean> toTest = givenEmptyMap();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfArrayWithLength4() throws Exception {
        final Integer[] toTest = givenArrayWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyArray() throws Exception {
        final Integer[] toTest = givenEmptyArray();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfCharSequenceWithLength4() throws Exception {
        final CharSequence toTest = givenCharSequenceWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyCharSequence() throws Exception {
        final CharSequence toTest = givenEmptyCharSequence();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfPathWithLength4() throws Exception {
        final Path toTest = givenPathWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyPath() throws Exception {
        final Path toTest = givenEmptyPath();

        assertThat(isEmpty(toTest), equalTo(true));
    }

    @Test
    public void isEmptyOfFileWithLength4() throws Exception {
        final File toTest = givenFileWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfEmptyFile() throws Exception {
        assertThat(() -> isEmpty(EMPTY_DIRECTORY.resolve("doesNotExist")), throwsException(UncheckedIOException.class, "java.nio.file.NoSuchFileException: .*doesNotExist"));
    }

    @Test
    public void isEmptyOfUnknownFileThrowsException() throws Exception {
        final File toTest = givenFileWithLength4();

        assertThat(isEmpty(toTest), equalTo(false));
    }

    @Test
    public void isEmptyOfUnknownTypeThrowsException() throws Exception {
        assertThat(() -> isEmpty(1), throwsException(IllegalArgumentException.class, "Could not check emptiness of 1."));
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
    protected static File givenFileWithLength4() {
        return givenPathWithLength4().toFile();
    }

    @Nonnull
    protected static File givenEmptyFile() {
        return givenEmptyPath().toFile();
    }


}
