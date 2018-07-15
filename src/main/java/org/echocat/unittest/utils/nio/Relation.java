package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Relation<T> extends Supplier<T> {

    @Nonnull
    @Override
    T get();

    @Nonnull
    static java.lang.Class<?> typeOf(@Nonnull Relation<?> relation) {
        if (relation instanceof Relation.Object<?>) {
            return relation.get().getClass();
        }
        if (relation instanceof Relation.Class<?>) {
            return ((Class<?>) relation).get();
        }
        throw new IllegalArgumentException("Could not handle relation: " + relation);
    }

    @Nullable
    static java.lang.Object targetOf(@Nonnull Relation<?> relation) {
        if (relation instanceof Relation.Object<?>) {
            return requireNonNull(relation.get());
        }
        return null;
    }

    @Nonnull
    static <T> Class<T> classRelationFor(@Nonnull java.lang.Class<T> type) {
        return () -> type;
    }

    @Nonnull
    static <T> Object<T> objectRelationFor(@Nonnull T object) {
        return () -> object;
    }

    @FunctionalInterface
    interface Object<T> extends Relation<T> {}

    @FunctionalInterface
    interface Class<T> extends Relation<java.lang.Class<T>> {}

}
