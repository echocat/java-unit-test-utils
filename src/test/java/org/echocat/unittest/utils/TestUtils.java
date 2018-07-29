package org.echocat.unittest.utils;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public final class TestUtils {

    public static class IterableImpl<T> implements Iterable<T> {

        private final Iterable<T> delegate;

        public IterableImpl(final Iterable<T> delegate) {
            this.delegate = delegate;
        }

        @Nonnull
        @Override
        public Iterator<T> iterator() {
            return delegate.iterator();
        }

    }

    @Nonnull
    public static Description givenDescription() {
        return new StringDescription();
    }

    @Nonnull
    public static Stream<Integer> givenStreamWithLength4() {
        return Stream.of(0, 1, 2, 3);
    }

    @Nonnull
    public static Stream<Integer> givenEmptyStream() {
        return Stream.empty();
    }

    @Nonnull
    public static Collection<Integer> givenCollectionOfLength4() {
        return Arrays.asList(0, 1, 2, 3);
    }

    @Nonnull
    public static Collection<Integer> givenEmptyCollection() {
        return Collections.emptyList();
    }

    @Nonnull
    public static Iterable<Integer> givenIterableOfLength4() {
        return new IterableImpl<>(Arrays.asList(0, 1, 2, 3));
    }

    @Nonnull
    public static Iterable<Integer> givenEmptyIterable() {
        return new IterableImpl<>(Collections.emptyList());
    }

    @Nonnull
    public static Iterator<Integer> givenIteratorOfLength4() {
        return Arrays.asList(0, 1, 2, 3).iterator();
    }

    @Nonnull
    public static Iterator<Integer> givenEmptyIterator() {
        return Collections.emptyIterator();
    }

    @Nonnull
    public static Map<Integer, Boolean> givenMapWithLength4() {
        final Map<Integer, Boolean> toTest = new HashMap<>();
        toTest.put(0, true);
        toTest.put(1, true);
        toTest.put(2, true);
        toTest.put(3, true);
        return toTest;
    }

    @Nonnull
    public static Map<Integer, Boolean> givenEmptyMap() {
        return Collections.emptyMap();
    }

    @Nonnull
    public static Object[] givenArrayWithLength3() {
        return new Object[]{0, 1, 2};
    }

    @Nonnull
    public static Object[] givenArrayWithLength4() {
        return new Object[]{0, 1, 2, 3};
    }

    @Nonnull
    public static Object[] givenEmptyArray() {
        return new Object[0];
    }

    @Nonnull
    public static boolean[] givenBooleanArrayWithLength4() {
        return new boolean[4];
    }

    @Nonnull
    public static byte[] givenByteArrayWithLength4() {
        return new byte[4];
    }

    @Nonnull
    public static char[] givenCharArrayWithLength4() {
        return new char[4];
    }

    @Nonnull
    public static short[] givenShortArrayWithLength4() {
        return new short[4];
    }

    @Nonnull
    public static int[] givenIntArrayWithLength4() {
        return new int[4];
    }

    @Nonnull
    public static long[] givenLongArrayWithLength4() {
        return new long[4];
    }

    @Nonnull
    public static float[] givenFloatArrayWithLength4() {
        return new float[4];
    }

    @Nonnull
    public static double[] givenDoubleArrayWithLength4() {
        return new double[4];
    }

    @Nonnull
    public static CharSequence givenCharSequenceWithLength4() {
        return "0123";
    }

    @Nonnull
    public static CharSequence givenEmptyCharSequence() {
        return "";
    }

    @Nonnull
    public static InputStream givenInputStreamWithLength4() {
        return new ByteArrayInputStream(new byte[]{'0', '1', '2', '3'});
    }

    @Nonnull
    public static InputStream givenEmptyInputStream() {
        return new ByteArrayInputStream(new byte[]{});
    }

    @Nonnull
    public static InputStream givenExceptionThrowingInputStream() throws IOException {
        final InputStream mock = mock(InputStream.class);
        doThrow(new IOException("test")).when(mock).read(any());
        return mock;
    }

    @Nonnull
    public static Reader givenReaderWithLength4() {
        return new InputStreamReader(givenInputStreamWithLength4());
    }

    @Nonnull
    public static Reader givenEmptyReader() {
        return new InputStreamReader(givenEmptyInputStream());
    }

    @Nonnull
    public static Reader givenExceptionThrowingReader() throws IOException {
        final Reader mock = mock(Reader.class);
        doThrow(new IOException("test")).when(mock).read((char[]) any());
        return mock;
    }

    @Nonnull
    public static URL givenURLWithLength4() {
        return requireNonNull(TestUtils.class.getResource("fileWithLength4"), "broken classpath");
    }

    @Nonnull
    public static URL givenEmptyURL() {
        return requireNonNull(TestUtils.class.getResource("emptyFile"), "broken classpath");
    }

    @Nonnull
    public static URL givenExceptionThrowingURL() throws IOException {
        return new URL("file:notExisting");
    }

    @Nonnull
    public static URI givenURIWithLength4() throws URISyntaxException {
        return givenURLWithLength4().toURI();
    }

    @Nonnull
    public static URI givenEmptyURI() throws URISyntaxException {
        return givenEmptyURL().toURI();
    }

    @Nonnull
    public static URI givenExceptionThrowingURI() throws URISyntaxException {
        return new URI("notExisting://abc");
    }

    @Nonnegative
    public static Set<String> childrenOf(@Nonnull Path path) throws Exception {
        try (final Stream<Path> stream = Files.list(path)) {
            return stream
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(toSet());
        }
    }

    @Nonnegative
    public static long numberOfChildrenOf(@Nonnull Path path) throws Exception {
        return numberOfChildrenOf(path, candidate -> true);
    }

    @Nonnegative
    public static long numberOfChildrenOf(@Nonnull Path path, @Nonnull Predicate<Path> matches) throws Exception {
        try (final Stream<Path> stream = Files.list(path)) {
            return stream
                .filter(matches)
                .count();
        }
    }

}
