package org.echocat.unittest.utils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Stream;

public abstract class TestSupport {

    protected static class IterableImpl<T> implements Iterable<T> {

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
    protected Stream<Integer> givenStreamWithLength4() {
        return Stream.of(0, 1, 2, 3);
    }

    @Nonnull
    protected Stream<Integer> givenEmptyStream() {
        return Stream.empty();
    }

    @Nonnull
    protected Collection<Integer> givenCollectionOfLength4() {
        return Arrays.asList(0, 1, 2, 3);
    }

    @Nonnull
    protected Collection<Integer> givenEmptyCollection() {
        return Collections.emptyList();
    }

    @Nonnull
    protected Iterable<Integer> givenIterableOfLength4() {
        return new IterableImpl<>(Arrays.asList(0, 1, 2, 3));
    }

    @Nonnull
    protected Iterable<Integer> givenEmptyIterable() {
        return new IterableImpl<>(Collections.emptyList());
    }

    @Nonnull
    protected Iterator<Integer> givenIteratorOfLength4() {
        return Arrays.asList(0, 1, 2, 3).iterator();
    }

    @Nonnull
    protected Iterator<Integer> givenEmptyIterator() {
        return Collections.emptyIterator();
    }

    @Nonnull
    protected Map<Integer, Boolean> givenMapWithLength4() {
        final Map<Integer, Boolean> toTest = new HashMap<>();
        toTest.put(0, true);
        toTest.put(1, true);
        toTest.put(2, true);
        toTest.put(3, true);
        return toTest;
    }

    @Nonnull
    protected Map<Integer, Boolean> givenEmptyMap() {
        return Collections.emptyMap();
    }

    @Nonnull
    protected Integer[] givenArrayWithLength4() {
        return new Integer[]{0, 1, 2, 3};
    }

    @Nonnull
    protected Integer[] givenEmptyArray() {
        return new Integer[0];
    }

    @Nonnull
    protected CharSequence givenCharSequenceWithLength4() {
        return "0123";
    }

    @Nonnull
    protected CharSequence givenEmptyCharSequence() {
        return "";
    }

}
