package org.echocat.unittest.utils.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;

public final class StringUtils {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    @Nonnull
    public static String toString(@Nullable Object what) {
        if (what == null) {
            return "";
        }
        if (what instanceof String) {
            return (String) what;
        }
        if (what instanceof char[]) {
            return toString((char[]) what);
        }
        if (what instanceof byte[]) {
            return toString((byte[]) what);
        }
        if (what instanceof Reader) {
            return toString((Reader) what);
        }
        if (what instanceof InputStream) {
            return toString((InputStream) what);
        }
        if (what instanceof URL) {
            return toString((URL) what);
        }
        if (what instanceof Path) {
            return toString((Path) what);
        }
        if (what instanceof File) {
            return toString((File) what);
        }
        return what.toString();
    }

    @Nonnull
    private static String toString(@Nullable @WillNotClose Reader what) {
        if (what == null) {
            return "";
        }
        final StringBuilder result = new StringBuilder();
        try {
            final char[] buf = new char[4096];
            int read = what.read(buf);
            while (read >= 0) {
                result.append(buf, 0, read);
                read = what.read(buf);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return result.toString();
    }

    @Nonnull
    private static String toString(@Nullable @WillNotClose InputStream what) {
        return toString(what != null ? new InputStreamReader(what, UTF8) : null);
    }

    @Nonnull
    private static String toString(@Nullable URL what) {
        if (what == null) {
            return "";
        }
        try (final InputStream is = what.openStream()) {
            return toString(is);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    private static String toString(@Nullable Path what) {
        if (what == null) {
            return "";
        }
        try (final InputStream is = Files.newInputStream(what)) {
            return toString(is);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    private static String toString(@Nullable File what) {
        if (what == null) {
            return "";
        }
        return toString(what.toPath());
    }

    @Nonnull
    private static String toString(@Nullable char[] what) {
        if (what == null) {
            return "";
        }
        return new String(what);
    }

    @Nonnull
    private static String toString(@Nullable byte[] what) {
        if (what == null) {
            return "";
        }
        return new String(what, UTF8);
    }

}
