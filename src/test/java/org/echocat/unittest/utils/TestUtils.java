package org.echocat.unittest.utils;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Stream;

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
    public static Integer[] givenArrayWithLength4() {
        return new Integer[]{0, 1, 2, 3};
    }

    @Nonnull
    public static Integer[] givenEmptyArray() {
        return new Integer[0];
    }

    @Nonnull
    public static CharSequence givenCharSequenceWithLength4() {
        return "0123";
    }

    @Nonnull
    public static CharSequence givenEmptyCharSequence() {
        return "";
    }

}
