package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.nio.TemporaryResourceBroker.ContentProducer;

import javax.annotation.Nonnull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface TemporaryDirectory {

    /**
     * Alias for {@link #ofName}.
     */
    @Nonnull
    String value() default "";

    /**
     * Will be the filename of the created temporary file on the disk.
     */
    @Nonnull
    String ofName() default "";


    /**
     * <p>Will use a method in test-suite of given name to generate the content.</p>
     *
     * <p>Signature have to be:<br>
     * <code>private void static generateContent({@link Path} target) throws {@link Exception} { ... }</code>
     * </p>
     */
    @Nonnull
    String usingGeneratorMethod() default "";

    @SuppressWarnings("InterfaceNeverImplemented")
    public interface Root {}

    public static class ContentProducerFactory {

        @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
        @Nonnull
        private static final Collection<Function<TemporaryDirectory, Optional<ContentProducer<Path>>>> INPUT_TO_PRODUCER = unmodifiableList(asList(
            ContentProducerFactory::createUsingGeneratorMethodFor
        ));

        @Nonnull
        public ContentProducer<Path> createFor(@Nonnull TemporaryDirectory input) {
            final List<ContentProducer<Path>> candidates = inputToProducer().stream()
                .map(candidate -> candidate.apply(input))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());

            if (candidates.isEmpty()) {
                return (relation, os) -> {
                };
            }
            if (candidates.size() == 1) {
                return candidates.get(0);
            }
            throw new IllegalArgumentException("The definition of given @TemporaryDirectory is ambiguous - it leads to more than one way to generate the content.");
        }

        @Nonnull
        protected Collection<Function<TemporaryDirectory, Optional<ContentProducer<Path>>>> inputToProducer() {
            return INPUT_TO_PRODUCER;
        }

        @Nonnull
        protected static Optional<ContentProducer<Path>> createUsingGeneratorMethodFor(@Nonnull TemporaryDirectory input) {
            if (input.usingGeneratorMethod().isEmpty()) {
                return empty();
            }
            return of((relation, os) -> {
                final Method method = generatorMethodOf(relation, input.usingGeneratorMethod());
                try {
                    method.invoke(relation instanceof Class ? null : relation, os);
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
                    throw new RuntimeException("Cloud not execute " + method + " to generate temporary directory content.", target != null ? target : e);
                }
            });
        }

        @Nonnull
        protected static Method generatorMethodOf(@Nonnull Object relation, @Nonnull String name) {
            final Class<?> relationType = relation instanceof Class ? (Class<?>) relation : relation.getClass();
            try {
                final Method method = relationType.getDeclaredMethod(name, Path.class);
                if (relation instanceof Class && !isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException("The definition of given @TemporaryDirectory reflects the method " + name + "(OutputStream os)" +
                        " which is not static but needs to be static.");
                }
                return method;
            } catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("The definition of given @TemporaryDirectory reflects a method " + name + "(OutputStream os) but it does not exist.", e);
            }
        }

    }

}
