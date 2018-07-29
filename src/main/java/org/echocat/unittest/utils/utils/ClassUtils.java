package org.echocat.unittest.utils.utils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

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

    @Nonnull
    public static Method methodBy(@Nonnull Class<?> source, @Nonnull String name, @Nonnull Class<?>... argumentTypes) {
        try {
            final Method result = source.getDeclaredMethod(name, argumentTypes);
            result.setAccessible(true);
            return result;
        } catch (final NoSuchMethodException e) {
            final NoSuchElementException ex = new NoSuchElementException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

}
