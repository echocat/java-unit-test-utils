package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;

public class ClassUtils {

    @Nonnull
    public static Class<?> typeOf(@Nonnull Object expected) {
        //noinspection ConstantConditions
        if (expected == null) {
            throw new NullPointerException("The provided expected value is null.");
        }
        if (!(expected instanceof Comparable)) {
            throw new IllegalArgumentException("The provided expected value is not of type " + Comparable.class.getName() + ".");
        }
        return expected.getClass();
    }

}
