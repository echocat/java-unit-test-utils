package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Stream.of;
import static java.util.stream.StreamSupport.stream;

public final class StreamUtils {

    @SafeVarargs
    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull T... input) {
        return of(input);
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Iterable<T> input) {
        return toStream(input.spliterator());
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Iterator<T> input) {
        return toStream(spliteratorUnknownSize(input, Spliterator.ORDERED));
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Spliterator<T> input) {
        return stream(input, false);
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Stream<T> input) {
        return input;
    }

}
