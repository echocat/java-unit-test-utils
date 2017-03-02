package org.echocat.unittest.utils.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;
import static java.util.Spliterators.spliteratorUnknownSize;

public final class StreamUtils {

    @Nonnull
    public static Stream<Object> toStream(@Nonnull Object input) {
        requireNonNull(input, "There are not objects provided.");
        if (input instanceof Object[]) {
            return toStream((Object[]) input);
        }
        if (input instanceof Collection) {
            //noinspection unchecked
            return toStream((Collection<Object>) input);
        }
        if (input instanceof Iterable) {
            //noinspection unchecked
            return toStream((Iterable<Object>) input);
        }
        if (input instanceof Iterator) {
            //noinspection unchecked
            return toStream((Iterator<Object>) input);
        }
        if (input instanceof Spliterator) {
            //noinspection unchecked
            return toStream((Spliterator<Object>) input);
        }
        if (input instanceof Stream) {
            //noinspection unchecked
            return (Stream<Object>) input;
        }
        throw new IllegalArgumentException("Could not handle input of type: " + input.getClass().getName());
    }

    @SafeVarargs
    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull T... input) {
        return Stream.of(input);
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Collection<T> input) {
        return input.stream();
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
        return StreamSupport.stream(input, false);
    }

    @Nonnull
    public static <T> Stream<T> toStream(@Nonnull Stream<T> input) {
        return input;
    }

}
