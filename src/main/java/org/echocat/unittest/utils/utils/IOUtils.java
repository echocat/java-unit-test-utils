package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

}
