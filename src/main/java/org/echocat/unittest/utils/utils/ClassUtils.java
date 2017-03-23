package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;

public final class ClassUtils {

    @Nonnull
    public static <T> Class<? extends T> typeOf(@Nonnull Class<T> requiredType, @Nonnull Object object) {
        //noinspection ConstantConditions
        if (object == null) {
            throw new NullPointerException("The provided object value is null.");
        }
        if (!requiredType.isInstance(object)) {
            throw new IllegalArgumentException("The provided object value is not of type " + Comparable.class.getName() + ".");
        }
        // noinspection unchecked
        return (Class<? extends T>) object.getClass();
    }

}
