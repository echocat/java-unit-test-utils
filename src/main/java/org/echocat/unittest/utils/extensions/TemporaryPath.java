package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ClassRelation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ObjectRelation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.Relation;

import javax.annotation.Nonnull;
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
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.Relation.targetOf;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.Relation.typeOf;

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
            throw new IllegalArgumentException("The element " + element + " is annotated with more then one valid annotations for temporary resource management: " + candidates);
        }

        @Nonnull
        public static <T> ContentProducer<T> methodBasedContentProducerFor(@Nonnull Annotation annotation, @Nonnull String name, @Nonnull Class<?> toArgumentType) {
            return (relation, os) -> {
                final Method method = generatorMethodFor(annotation, relation, name, toArgumentType);
                try {
                    if (isStatic(method.getModifiers())) {
                        method.invoke(null, os);
                    } else {
                        method.invoke(relation.get(), os);
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
                    throw new RuntimeException("Cloud not execute " + method + " to generate temporary file content.", target != null ? target : e);
                }
            };
        }

        @Nonnull
        public static Method generatorMethodFor(@Nonnull Annotation annotation, @Nonnull Relation<?> relation, @Nonnull String name, @Nonnull Class<?> toArgumentType) {
            final Class<?> relationType = typeOf(relation);
            try {
                final Method method = relationType.getDeclaredMethod(name, toArgumentType);
                if (relation instanceof ClassRelation && !isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException("The definition of given @" + annotation.annotationType().getSimpleName()
                        + " reflects the method " + name + "(" + toArgumentType.getName() + ") which is not static but needs to be static.");
                }
                if (isAbstract(method.getModifiers())) {
                    throw new IllegalArgumentException("The definition of given @" + annotation.annotationType().getSimpleName()
                        + " reflects the method " + name + "(" + toArgumentType.getName() + ") which is abstract but needs to be not abstract.");
                }
                method.setAccessible(true);
                return method;
            } catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("The definition of given @" + annotation.annotationType().getSimpleName()
                    + " reflects a method " + name + "(" + toArgumentType.getName() + ") but it does not exist.", e);
            }
        }
    }

}
