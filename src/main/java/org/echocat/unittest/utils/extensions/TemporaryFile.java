package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryFile.Provider;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.echocat.unittest.utils.nio.Relation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Thread.currentThread;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.methodBasedContentProducerFor;
import static org.echocat.unittest.utils.nio.Relation.typeOf;
import static org.echocat.unittest.utils.utils.IOUtils.copy;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@TemporaryPath(provider = Provider.class)
public @interface TemporaryFile {

    /**
     * Will be the filename of the created temporary file on the disk.
     */
    @Nonnull
    String ofName() default "test";

    /**
     * <p>Creates the content of this file as a copy of this file in classpath.</p>
     *
     * <p>If {@link #relativeTo} is set to a specific class the resource will be searched in the same
     * package and classloader as this class. Otherwise the root of the classpath will be assumed.</p>
     *
     * @see #relativeTo
     * @see #withContent
     * @see #withBinaryContent
     * @see #withRandomContentOfLength
     * @see #usingGeneratorMethod
     */
    @Nonnull
    String fromClasspath() default "";

    /**
     * <p>Sets the relation of files in classpath. See {@link #fromClasspath} for more details.</p>
     *
     * <p>If set to {@link TestClass TestClass} the provided classpath resource (by {@link #fromClasspath}) will
     * be resolved in relation to the current test class.</p>
     *
     * <p>If set to {@link Root Root} the provided classpath resource (by {@link #fromClasspath}) will
     * be resolved from the root of the whole classpath.</p>
     *
     * @see #fromClasspath
     */
    Class<?> relativeTo() default TestClass.class;

    /**
     * <p>Specifies a content of this file (encoded in UTF8).</p>
     *
     * @see #fromClasspath
     * @see #withBinaryContent
     * @see #withRandomContentOfLength
     * @see #usingGeneratorMethod
     */
    @Nonnull
    String withContent() default "";

    /**
     * <p>Specifies a content of this file in bytes.</p>
     *
     * @see #fromClasspath
     * @see #withContent
     * @see #withRandomContentOfLength
     * @see #usingGeneratorMethod
     */
    @Nonnull
    byte[] withBinaryContent() default {};

    /**
     * <p>Will generate the file content with given length with random content.</p>
     *
     * @see #fromClasspath
     * @see #withContent
     * @see #withBinaryContent
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
     * @see #withBinaryContent
     * @see #withRandomContentOfLength
     */
    @Nonnull
    String usingGeneratorMethod() default "";

    @SuppressWarnings("InterfaceNeverImplemented")
    interface TestClass {}
    @SuppressWarnings("InterfaceNeverImplemented")
    interface Root {}

    class Provider implements TemporaryPath.Provider<TemporaryFile> {

        @Nonnull
        private static final Random RANDOM = new Random(666L);
        @Nonnull
        private static final Collection<Function<TemporaryFile, Optional<ContentProducer<OutputStream>>>> INPUT_TO_PRODUCER = unmodifiableList(asList(
            Provider::createFromClasspathFor,
            Provider::createWithContentFor,
            Provider::createWithBinaryContentFor,
            Provider::createWithRandomContentOfLengthFor,
            Provider::createUsingGeneratorMethodFor
        ));

        @Nonnull
        @Override
        public Path provide(@Nonnull TemporaryFile forAnnotation, @Nonnull Relation<?> relation, @Nonnull TemporaryPathBroker using) throws Exception {
            final ContentProducer<OutputStream> contentProducer = contentProducerFor(forAnnotation);
            return using.newFile(forAnnotation.ofName(), relation, contentProducer);
        }

        @Nonnull
        public ContentProducer<OutputStream> contentProducerFor(@Nonnull TemporaryFile input) {
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
                Class<?> baseClass = input.relativeTo();
                if (baseClass.equals(Root.class)) {
                    try (final InputStream is = currentThread().getContextClassLoader().getResourceAsStream(path)) {
                        copy(is, os);
                    }
                } else {
                    if (baseClass.equals(TestClass.class)) {
                        baseClass = typeOf(relation);
                    }
                    try (final InputStream is = baseClass.getResourceAsStream(path)) {
                        copy(is, os);
                    }
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
        protected static Optional<ContentProducer<OutputStream>> createWithBinaryContentFor(@Nonnull TemporaryFile input) {
            if (input.withBinaryContent().length == 0) {
                return empty();
            }
            return of((relation, os) -> os.write(input.withBinaryContent()));
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
            return of(methodBasedContentProducerFor(input, input.usingGeneratorMethod(), OutputStream.class));
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
