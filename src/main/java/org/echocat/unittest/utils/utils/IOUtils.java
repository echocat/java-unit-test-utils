package org.echocat.unittest.utils.utils;

import org.echocat.unittest.utils.utils.ExceptionUtils.Execution;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import static org.echocat.unittest.utils.utils.ExceptionUtils.executeSafe;

public final class IOUtils {

    static final int BUFFER_SIZE = 8192;

    @Nonnegative
    public static long copy(@Nonnull @WillNotClose InputStream is, @Nonnull @WillNotClose OutputStream os) throws IOException {
        long nread = 0L;
        final byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = is.read(buf)) > 0) {
            os.write(buf, 0, n);
            nread += n;
        }
        return nread;
    }

    @Nonnull
    public static Execution<?extends Exception> toCloseExecution(@Nonnull AutoCloseable closeable) {
        return closeable::close;
    }

    public static void closeAll(@Nonnull Collection<? extends AutoCloseable> closeables) throws Exception {
        executeSafe(Exception.class, closeables.stream()
            .map(IOUtils::toCloseExecution)
        );
    }

}
