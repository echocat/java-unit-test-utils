package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.nio.TemporaryResourceBroker.ContentProducer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Thread.currentThread;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.reflect.Modifier.isStatic;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.echocat.unittest.utils.utils.IOUtils.copy;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface TemporaryFile {

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
     * <p>Creates the content of this file as a copy of this file in classpath.</p>
     *
     * <p>If {@link #relativeTo} is set to a specific class the resource will be searched in the same
     * package and classloader as this class. Otherwise the root of the classpath will be assumed.</p>
     *
     * @see #relativeTo
     * @see #withContent
     * @see #withRandomContentOfLength
     * @see #usingGeneratorMethod
     */
    @Nonnull
    String fromClasspath() default "";

    /**
     * Sets the relation of files in classpath. See {@link #fromClasspath} for more details.
     *
     * @see #fromClasspath
     */
    Class<?> relativeTo() default Root.class;

    /**
     * <p>Specifies a content of this file (encoded in UTF8).</p>
     *
     * @see #fromClasspath
     * @see #withRandomContentOfLength
     * @see #usingGeneratorMethod
     */
    @Nonnull
    String withContent() default "";

    /**
     * <p>Will generate the file content with given length with random content.</p>
     *
     * @see #fromClasspath
     * @see #withContent
     * @see #usingGeneratorMethod
     */
    @Nonnegative
    long withRandomContentOfLength() default 0;

    /**
     * <p>Will use a method in test-suite of given name to generate the content.</p>
     *
     * <p>Signature have to be:<br>
     * <code>private void static generateContent({@link OutputStream} os) throws {@link Exception} { ... }</code>
     * </p>
     *
     * @see #fromClasspath
     * @see #withContent
     * @see #withRandomContentOfLength
     */
    @Nonnull
    String usingGeneratorMethod() default "";

    @SuppressWarnings("InterfaceNeverImplemented")
    public interface Root {}

    public static class ContentProducerFactory {

        @Nonnull
        private static final Random RANDOM = new Random(666L);
        @Nonnull
        private static final Collection<Function<TemporaryFile, Optional<ContentProducer<OutputStream>>>> INPUT_TO_PRODUCER = unmodifiableList(asList(
            ContentProducerFactory::createFromClasspathFor,
            ContentProducerFactory::createWithContentFor,
            ContentProducerFactory::createWithRandomContentOfLengthFor,
            ContentProducerFactory::createUsingGeneratorMethodFor
        ));

        @Nonnull
        public ContentProducer<OutputStream> createFor(@Nonnull TemporaryFile input) {
            final List<ContentProducer<OutputStream>> candidates = inputToProducer().stream()
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
            throw new IllegalArgumentException("The definition of given @TemporaryFile is ambiguous - it leads to more than one way to generate the content.");
        }

        @Nonnull
        protected Collection<Function<TemporaryFile, Optional<ContentProducer<OutputStream>>>> inputToProducer() {
            return INPUT_TO_PRODUCER;
        }

        @Nonnull
        protected static Optional<ContentProducer<OutputStream>> createFromClasspathFor(@Nonnull TemporaryFile input) {
            if (input.fromClasspath().isEmpty()) {
                return empty();
            }
            final String path = removeLeadingSlashes(input.fromClasspath());
            return of((relation, os) -> {
                try (final InputStream is = input.relativeTo().equals(Root.class)
                    ? currentThread().getContextClassLoader().getResourceAsStream(path)
                    : input.relativeTo().getResourceAsStream(path)) {
                    copy(is, os);
                }
            });
        }

        @Nonnull
        protected static Optional<ContentProducer<OutputStream>> createWithContentFor(@Nonnull TemporaryFile input) {
            if (input.withContent().isEmpty()) {
                return empty();
            }
            return of((relation, os) -> os.write(input.withContent().getBytes(UTF_8)));
        }

        @Nonnull
        protected static Optional<ContentProducer<OutputStream>> createWithRandomContentOfLengthFor(@Nonnull TemporaryFile input) {
            if (input.withRandomContentOfLength() <= 0) {
                return empty();
            }
            return of((relation, os) -> {
                final byte[] buf = new byte[4096];
                for (long written = 0; written < input.withRandomContentOfLength(); written += buf.length) {
                    RANDOM.nextBytes(buf);
                    if (written + buf.length > input.withRandomContentOfLength()) {
                        os.write(buf, 0, (int) (input.withRandomContentOfLength() - written));
                    } else {
                        os.write(buf);
                    }
                }
            });
        }

        @Nonnull
        protected static Optional<ContentProducer<OutputStream>> createUsingGeneratorMethodFor(@Nonnull TemporaryFile input) {
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
                    throw new RuntimeException("Cloud not execute " + method + " to generate temporary file content.", target != null ? target : e);
                }
            });
        }

        @Nonnull
        protected static Method generatorMethodOf(@Nonnull Object relation, @Nonnull String name) {
            final Class<?> relationType = relation instanceof Class ? (Class<?>) relation : relation.getClass();
            try {
                final Method method = relationType.getDeclaredMethod(name, OutputStream.class);
                if (relation instanceof Class && !isStatic(method.getModifiers())) {
                    throw new IllegalArgumentException("The definition of given @TemporaryFile reflects the method " + name + "(OutputStream os)" +
                        " which is not static but needs to be static.");
                }
                return method;
            } catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("The definition of given @TemporaryFile reflects a method " + name + "(OutputStream os) but it does not exist.", e);
            }
        }

        @Nonnull
        protected static String removeLeadingSlashes(@Nonnull String input) {
            String output = input;
            while (output.startsWith("/")) {
                output = output.substring(1);
            }
            return output;
        }

    }

}
