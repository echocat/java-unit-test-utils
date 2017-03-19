package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static org.echocat.unittest.utils.utils.IOUtils.BUFFER_SIZE;

public final class SizeUtils {

    @Nonnegative
    public static long sizeOf(@Nullable Object what) {
        if (what == null) {
            return 0;
        }
        if (what instanceof Stream) {
            return ((Stream<?>) what).count();
        }
        if (what instanceof Path) {
            return sizeOf((Path) what);
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
        if (what instanceof File) {
            return sizeOf(((File) what).toPath());
        }
        if (what instanceof InputStream) {
            return sizeOf((InputStream) what);
        }
        if (what instanceof Reader) {
            return sizeOf((Reader) what);
        }
        if (what instanceof URL) {
            return sizeOf((URL) what);
        }
        if (what instanceof URI) {
            return sizeOf((URI) what);
        }
        throw new IllegalArgumentException("Could not get size of " + what.getClass().getName() + ".");
    }

    @Nonnegative
    private static long sizeOf(@Nonnull Iterator<?> iterator) {
        long result = 0;
        while (iterator.hasNext()) {
            iterator.next();
            result++;
        }
        return result;
    }

    @Nonnegative
    private static long sizeOf(@Nonnull Path path) {
        try {
            return Files.size(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnegative
    private static long sizeOf(@Nonnull InputStream is) {
        long nread = 0L;
        final byte[] buf = new byte[BUFFER_SIZE];
        int n;
        try {
            while ((n = is.read(buf)) > 0) {
                nread += n;
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return nread;
    }

    @Nonnegative
    private static long sizeOf(@Nonnull Reader reader) {
        long nread = 0L;
        final char[] buf = new char[BUFFER_SIZE];
        int n;
        try {
            while ((n = reader.read(buf)) > 0) {
                nread += n;
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return nread;
    }

    @Nonnegative
    private static long sizeOf(@Nonnull URL url) {
        try (final InputStream is = url.openStream()) {
            return sizeOf(is);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnegative
    private static long sizeOf(@Nonnull URI uri) {
        try {
            return sizeOf(uri.toURL());
        } catch (final MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static boolean isNotEmpty(@Nullable Object what) {
        return !isEmpty(what);
    }

    public static boolean isEmpty(@Nullable Object what) {
        if (what == null) {
            return true;
        }
        if (what instanceof Stream) {
            return ((Stream<?>) what).count() == 0;
        }
        if (what instanceof Path) {
            return sizeOf((Path) what) == 0;
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
        if (what instanceof File) {
            return sizeOf(((File) what).toPath()) == 0;
        }
        return sizeOf(what) == 0;
    }

    @Nonnegative
    private static boolean isEmpty(@Nonnull Iterator<?> iterator) {
        return !iterator.hasNext();
    }

}
