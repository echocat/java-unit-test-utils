package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.nio.Relation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.echocat.unittest.utils.nio.Relation.typeOf;

@Target({ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface TemporaryPath {

    Class<? extends Provider<?>> provider();

    @FunctionalInterface
    interface Provider<A extends Annotation> {
        @Nonnull
        Path provide(@Nonnull A forAnnotation, @Nonnull Relation<?> relation, @Nonnull TemporaryPathBroker using) throws Exception;
    }

    class Utils {

        @Nonnull
        public static Path provide(@Nonnull Annotation annotation, @Nonnull Relation<?> relation, @Nonnull TemporaryPathBroker using) throws Exception {
            final TemporaryPath parent = annotation.annotationType().getAnnotation(TemporaryPath.class);
            if (parent == null) {
                throw new IllegalStateException("Annotation @" + annotation.annotationType().getName() + " is not annotated with @" + TemporaryPath.class.getName() + ".");
            }
            final Class<? extends Provider<?>> provider = parent.provider();
            //noinspection rawtypes
            final Constructor<? extends Provider> constructor = provider.getDeclaredConstructor();
            constructor.setAccessible(true);
            //noinspection unchecked
            return constructor.newInstance().provide(annotation, relation, using);
        }

        @Nonnull
        public static Optional<Annotation> findTemporaryPathEnabledAnnotation(@Nonnull AnnotatedElement element) {
            final List<Annotation> candidates = new ArrayList<>(1);
            for (final Annotation candidate : element.getAnnotations()) {
                if (candidate.annotationType().getAnnotation(TemporaryPath.class) != null) {
                    candidates.add(candidate);
                }
            }
            if (candidates.isEmpty()) {
                return empty();
            }
            if (candidates.size() == 1) {
                return of(candidates.get(0));
            }
            throw new IllegalArgumentException("The element " + element + " is annotated with more than one valid annotations for temporary resource management: " + candidates);
        }

        @Nonnull
        public static <T> ContentProducer<T> methodBasedContentProducerFor(
            @Nonnull Class<? extends Annotation> annotationType,
            @Nonnull String name,
            @Nonnull Class<T> toArgumentType
        ) {
            return (relation, argument) -> {
                final Method method = generatorMethodFor(annotationType, relation, name, toArgumentType);
                try {
                    if (isStatic(method.getModifiers())) {
                        method.invoke(null, argument);
                    } else {
                        method.invoke(relation.get(), argument);
                    }
                } catch (final Exception e) {
                    final Throwable target = e instanceof InvocationTargetException ? ((InvocationTargetException) e).getTargetException() : null;
                    if (target instanceof Error) {
                        //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                        throw (Error) target;
                    }
                    if (target instanceof RuntimeException) {
                        //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                        throw (RuntimeException) target;
                    }
                    if (target instanceof IOException) {
                        //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                        throw new UncheckedIOException(target.getMessage(), (IOException) target);
                    }
                    final Throwable cause = target != null ? target : e;
                    throw new RuntimeException(cause.getMessage(), cause);
                }
            };
        }

        @Nonnull
        public static Method generatorMethodFor(@Nonnull Class<? extends Annotation> annotationType, @Nonnull Relation<?> relation, @Nonnull String name, @Nonnull Class<?> toArgumentType) {
            final Class<?> relationType = typeOf(relation);
            try {
                final Method method = relationType.getDeclaredMethod(name, toArgumentType);
                if (relation instanceof Relation.Class && !isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException("The definition of given @" + annotationType.getSimpleName()
                        + " reflects the method " + name + "(" + toArgumentType.getName() + ") which is not static but needs to be static.");
                }
                method.setAccessible(true);
                return method;
            } catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("The definition of given @" + annotationType.getSimpleName()
                    + " reflects a method " + name + "(" + toArgumentType.getName() + ") but it does not exist.", e);
            }
        }
    }

}
