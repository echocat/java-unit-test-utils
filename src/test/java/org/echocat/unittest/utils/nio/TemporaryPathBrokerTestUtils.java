package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public class TemporaryPathBrokerTestUtils {

    @Nonnull
    public static TemporaryPathBroker temporaryPathBrokerFor(@Nonnull Path baseDirectory) {
        return new TemporaryPathBroker(baseDirectory);
    }

}