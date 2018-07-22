package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static org.echocat.unittest.utils.nio.Wrapping.wrappedTypeOf;
import static org.echocat.unittest.utils.utils.ClassUtils.methodBy;

public interface WrappedEvent {

    @Nonnull
    Type type();

    @Nonnull
    List<Object> arguments();

    @FunctionalInterface
    interface Interceptor {

        @Nonnull
        default Optional<Object> before(@Nonnull WrappedEvent event) throws Exception {
            return Optional.empty();
        }

        @Nonnull
        Optional<Object> after(@Nonnull WrappedEvent event, @Nullable Object result) throws Exception ;

    }

    @FunctionalInterface
    interface InterceptorEnabled {

        @Nonnull
        Optional<Interceptor> interceptor();

    }

    @Nonnull
    static WrappedEvent eventOf(@Nonnull Type type, @Nonnull Object... arguments) {
        return new Impl(type, arguments);
    }

    @Nonnull
    static Type eventTypeOf(@SuppressWarnings("rawtypes") @Nonnull Class<? extends Wrapping> source, String methodName, Class<?>... argumentTypes) {
        final Method method = methodBy(source, methodName, argumentTypes);
        final Class<?> sourceType = wrappedTypeOf(source);
        //noinspection unchecked,rawtypes
        return new Type(
            sourceType,
            methodName,
            asList(argumentTypes),
            method.getReturnType(),
            asList((Class[]) method.getExceptionTypes())
        );
    }

    @Immutable
    class Type {

        @Nonnull
        private final Class<?> sourceType;
        @Nonnull
        private final String name;
        @Nonnull
        private final List<Class<?>> argumentTypes;
        @Nonnull
        private final Class<?> returnType;
        @Nonnull
        private final Set<Class<? extends Throwable>> allowedThrowableTypes;

        protected Type(
            @Nonnull Class<?> sourceType,
            @Nonnull String name,
            @Nonnull Collection<Class<?>> argumentTypes,
            @Nonnull Class<?> returnType,
            @Nonnull Collection<Class<? extends Throwable>> allowedThrowableTypes
        ) {
            this.sourceType = sourceType;
            this.name = name;
            this.argumentTypes = unmodifiableList(new ArrayList<>(argumentTypes));
            this.returnType = returnType;
            this.allowedThrowableTypes = unmodifiableSet(new HashSet<>(allowedThrowableTypes));
        }

        @Nonnull
        public Class<?> sourceType() {
            return sourceType;
        }

        @Nonnull
        public String name() {
            return name;
        }

        @Nonnull
        public List<Class<?>> argumentTypes() {
            return argumentTypes;
        }

        @Nonnull
        public Class<?> returnType() {
            return returnType;
        }

        @Nonnull
        public Set<Class<? extends Throwable>> allowedThrowableTypes() {
            return allowedThrowableTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof Type)) { return false; }
            final Type type = (Type) o;
            return Objects.equals(sourceType, type.sourceType) &&
                Objects.equals(name, type.name) &&
                Objects.equals(argumentTypes, type.argumentTypes) &&
                Objects.equals(returnType, type.returnType) &&
                Objects.equals(allowedThrowableTypes, type.allowedThrowableTypes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType, name, argumentTypes, returnType, allowedThrowableTypes);
        }

    }

    @Immutable
    class Impl implements WrappedEvent {

        @Nonnull
        private final Type type;
        @Nonnull
        private final List<Object> arguments;

        private Impl(@Nonnull Type type, @Nonnull Object... arguments) {
            this.type = type;
            this.arguments = unmodifiableList(asList(arguments));
        }

        @Nonnull
        @Override
        public Type type() {
            return type;
        }

        @Nonnull
        @Override
        public List<Object> arguments() {
            return arguments;
        }

    }

}
