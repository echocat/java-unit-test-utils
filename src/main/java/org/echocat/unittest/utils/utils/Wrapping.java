package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Wrapping<T> {

    @Nonnull
    T wrapped();

    @Nonnull
    static <T> T deepUnwrap(@Nonnull Class<T> requiredType, @Nonnull T input) {
        T current = input;
        while (current instanceof Wrapping<?>) {
            final Object candidate = ((Wrapping<?>) current).wrapped();
            if (!requiredType.isInstance(candidate)) {
                break;
            }
            current = requiredType.cast(candidate);
        }
        return current;
    }

}
