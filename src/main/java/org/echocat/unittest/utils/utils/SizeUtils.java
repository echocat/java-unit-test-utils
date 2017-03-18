package org.echocat.unittest.utils.utils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public final class SizeUtils {

    @Nonnegative
    public static long sizeOf(@Nullable Object what) {
        if (what == null) {
            return 0;
        }
        if (what instanceof Stream) {
            return sizeOf((Stream<?>) what);
        }
        if (what instanceof Collection) {
            return ((Collection<?>) what).size();
        }
        if (what instanceof Iterable) {
            return sizeOf(((Iterable<?>) what).iterator());
        }
        if (what instanceof Iterator) {
            return sizeOf((Iterator<?>) what);
        }
        if (what instanceof Map) {
            return ((Map<?, ?>) what).size();
        }
        if (what instanceof Object[]) {
            return ((Object[]) what).length;
        }
        if (what instanceof CharSequence) {
            return ((CharSequence) what).length();
        }
        if (what instanceof Path) {
            return sizeOf((Path) what);
        }
        if (what instanceof File) {
            return sizeOf(((File) what).toPath());
        }
        throw new IllegalArgumentException("Could not get size of " + what + ".");
    }

    @Nonnegative
    public static long sizeOf(@Nullable Iterator<?> iterator) {
        long result = 0;
        while (iterator != null && iterator.hasNext()) {
            iterator.next();
            result++;
        }
        return result;
    }

    @Nonnegative
    public static long sizeOf(@Nullable Stream<?> stream) {
        if (stream == null) {
            return 0;
        }
        return stream.count();
    }

    @Nonnegative
    public static long sizeOf(@Nullable Path path) {
        if (path == null) {
            return 0;
        }
        try {
            return Files.size(path);
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not get size of '" + path + "'.", e);
        }
    }

    @Nonnegative
    public static boolean isEmpty(@Nullable Object what) {
        if (what == null) {
            return true;
        }
        if (what instanceof Stream) {
            return isEmpty((Stream<?>) what);
        }
        if (what instanceof Collection) {
            return ((Collection<?>) what).isEmpty();
        }
        if (what instanceof Iterable) {
            return isEmpty(((Iterable<?>) what).iterator());
        }
        if (what instanceof Iterator) {
            return isEmpty((Iterator<?>) what);
        }
        if (what instanceof Map) {
            return ((Map<?, ?>) what).isEmpty();
        }
        if (what instanceof Object[]) {
            return ((Object[]) what).length == 0;
        }
        if (what instanceof CharSequence) {
            return ((CharSequence) what).length() == 0;
        }
        if (what instanceof Path) {
            return isEmpty((Path) what);
        }
        if (what instanceof File) {
            return isEmpty(((File) what).toPath());
        }
        throw new IllegalArgumentException("Could not check emptiness of " + what + ".");
    }

    @Nonnegative
    public static boolean isEmpty(@Nullable Iterator<?> iterator) {
        return iterator != null && iterator.hasNext();
    }

    @Nonnegative
    public static boolean isEmpty(@Nullable Stream<?> stream) {
        return sizeOf(stream) == 0;
    }

    @Nonnegative
    public static boolean isEmpty(@Nullable Path path) {
        return sizeOf(path) == 0;
    }

}
