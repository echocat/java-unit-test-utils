package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@FunctionalInterface
public
interface Interceptor {

    @Nonnull
    default Optional<Object> before(@Nonnull Event event) throws Exception {
        return Optional.empty();
    }

    @Nonnull
    Optional<Object> after(@Nonnull Event event, @Nullable Object result) throws Exception ;

    @FunctionalInterface
    interface InterceptorEnabled {

        @Nonnull
        Optional<Interceptor> interceptor();

    }
}
