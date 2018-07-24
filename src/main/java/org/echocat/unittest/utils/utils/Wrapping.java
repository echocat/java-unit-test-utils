package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Wrapping<T> {

    @Nonnull
    T wrapped();

}
