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

    @SuppressWarnings("OverlyComplexMethod")
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
        final AtomicLong result = new AtomicLong();
        if (stream != null) {
            stream.forEach(o -> result.incrementAndGet());
        }
        return result.get();
    }

    @Nonnegative
    public static long sizeOf(@Nullable Path path) {
        if (path == null) {
            return 0;
        }
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not get size of '" + path + "'.", e);
        }
    }

}
