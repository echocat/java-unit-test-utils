package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryDirectory.Provider;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.echocat.unittest.utils.nio.Relation;

import javax.annotation.Nonnull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.methodBasedContentProducerFor;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@TemporaryPath(provider = Provider.class)
public @interface TemporaryDirectory {

    /**
     * Will be the filename of the created temporary file on the disk.
     */
    @Nonnull
    String ofName() default "test";

    /**
     * <p>Will use a method in test-suite of given name to generate the content.</p>
     *
     * <p>Signature have to be:<br>
     * <code>private void static generateContent({@link Path} target) throws {@link Exception} { ... }</code>
     * </p>
     */
    @Nonnull
    String usingGeneratorMethod() default "";

    class Provider implements TemporaryPath.Provider<TemporaryDirectory> {

        @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
        @Nonnull
        private static final Collection<Function<TemporaryDirectory, Optional<ContentProducer<Path>>>> INPUT_TO_PRODUCER = unmodifiableList(asList(
            Provider::createUsingGeneratorMethodFor
        ));

        @Nonnull
        @Override
        public Path provide(@Nonnull TemporaryDirectory forAnnotation, @Nonnull Relation<?> relation, @Nonnull TemporaryPathBroker using) throws Exception {
            final ContentProducer<Path> contentProducer = contentProducerFor(forAnnotation);
            return using.newDirectory(forAnnotation.ofName(), relation, contentProducer);
        }

        @Nonnull
        public ContentProducer<Path> contentProducerFor(@Nonnull TemporaryDirectory input) {
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
            return of(methodBasedContentProducerFor(input, input.usingGeneratorMethod(), Path.class));
        }

    }

}
