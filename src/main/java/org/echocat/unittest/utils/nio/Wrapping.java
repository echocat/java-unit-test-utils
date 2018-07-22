package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@FunctionalInterface
public interface Wrapping<T> {

    @Nonnull
    T wrapped();

    @Nonnull
    static Class<?> wrappedTypeOf(@SuppressWarnings("rawtypes") @Nonnull Class<? extends Wrapping> wrapping) {
        for (final Type candidate : wrapping.getGenericInterfaces()) {
            if (candidate instanceof ParameterizedType) {
                final ParameterizedType parameterized = (ParameterizedType) candidate;
                final Type rawType = parameterized.getRawType();
                if (rawType instanceof Class && Wrapping.class.isAssignableFrom((Class<?>) rawType)) {
                    final Type argumentCandidate = parameterized.getActualTypeArguments()[0];
                    if (argumentCandidate instanceof Class) {
                        return (Class<?>) argumentCandidate;
                    }
                    throw new IllegalArgumentException("Can only accept explicit argument types of " + Wrapping.class + ".");
                }
            }
        }
        final Class<?> superClass = wrapping.getSuperclass();
        if (Wrapping.class.isAssignableFrom(superClass)) {
            //noinspection unchecked
            return wrappedTypeOf((Class<? extends Wrapping<?>>) superClass);
        }
        for (final Class<?> implementing : wrapping.getInterfaces()) {
            if (Wrapping.class.isAssignableFrom(implementing)) {
                //noinspection unchecked
                return wrappedTypeOf((Class<? extends Wrapping<?>>) superClass);
            }
        }
        throw new IllegalArgumentException(wrapping + " does not direct implement " + Wrapping.class + ".");
    }

}
