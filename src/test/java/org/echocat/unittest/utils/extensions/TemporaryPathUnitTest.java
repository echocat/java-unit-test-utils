package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryPath.Provider;
import org.echocat.unittest.utils.extensions.TemporaryPath.Utils;
import org.echocat.unittest.utils.matchers.OptionalMatchers;
import org.echocat.unittest.utils.matchers.WhereValueOf;
import org.echocat.unittest.utils.nio.Relation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.findTemporaryPathEnabledAnnotation;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.generatorMethodFor;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.methodBasedContentProducerFor;
import static org.echocat.unittest.utils.extensions.TemporaryPath.Utils.provide;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isAbsent;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.whereContentMatches;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.matchers.WhereValueOf.whereValueOf;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.nio.Relation.objectRelationFor;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.temporaryPathBrokerFor;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemporaryPathUnitTest {

    static class UtilsUnitTest {

        @Test
        void provide_calls_Provider_a_expected() throws Exception {
            try (final TemporaryPathBroker broker = temporaryPathBrokerFor("test")) {
                final TestAnnotation annotation = givenAnnotationFor("workingField");

                final Path actual = provide(annotation, Relation.classRelationFor(TestFields.class), broker);

                assertThat(actual, isEqualTo(Paths.get("1")));
            }
        }

        @Test
        void provide_will_fail_if_TemporaryPathAnnotation_is_missing() throws Exception {
            try (final TemporaryPathBroker broker = temporaryPathBrokerFor("test")) {
                final WithoutTemporaryPathAnnotation annotation = givenBrokenAnnotationFor("withoutTemporaryPathAnnotationField");

                assertThat(() -> provide(annotation, Relation.classRelationFor(TestFields.class), broker),
                    throwsException(IllegalStateException.class, "Annotation @.*WithoutTemporaryPathAnnotation is not annotated.*"));
            }
        }

        @Test
        void findTemporaryPathEnabledAnnotation_works_as_expected() throws Exception {
            final Field field = TestFields.class.getDeclaredField("workingField");

            final Optional<Annotation> actual = findTemporaryPathEnabledAnnotation(field);

            assertThat(actual, whereContentMatches(whereValueOf(Annotation::annotationType, "type", isEqualTo(TestAnnotation.class))));
        }

        @Test
        void findTemporaryPathEnabledAnnotation_cannot_find_Annotations() throws Exception {
            final Field field = TestFields.class.getDeclaredField("fieldWithNoAnnotation");

            final Optional<Annotation> actual = findTemporaryPathEnabledAnnotation(field);

            assertThat(actual, isAbsent());
        }

        @Test
        void findTemporaryPathEnabledAnnotation_will_fail_on_ambiguous_Annotations() throws Exception {
            final Field field = TestFields.class.getDeclaredField("ambiguousField");

            assertThat(() -> findTemporaryPathEnabledAnnotation(field),
                throwsException(IllegalArgumentException.class, "The element .+ambiguousField is annotated with more than one.+"));
        }

        @Test
        void methodBasedContentProducerFor_works_with_instance() throws Exception {
            final AtomicLong drain = new AtomicLong();

            final ContentProducer<AtomicLong> actual = methodBasedContentProducerFor(TestAnnotation.class, "instanceGeneratorMethod", AtomicLong.class);
            actual.produce(objectRelationFor(this), drain);

            assertThat(drain.get(), isEqualTo(111L));
        }

        @Test
        void methodBasedContentProducerFor_works_with_static() throws Exception {
            final AtomicLong drain = new AtomicLong();

            final ContentProducer<AtomicLong> actual = methodBasedContentProducerFor(TestAnnotation.class, "staticGeneratorMethod", AtomicLong.class);
            actual.produce(classRelationFor(this), drain);

            assertThat(drain.get(), isEqualTo(222L));
        }

        @Test
        void methodBasedContentProducerFor_handles_Errors() throws Exception {
            final ContentProducer<Object> instance = methodBasedContentProducerFor(TestAnnotation.class, "throwingErrorGeneratorMethod", Object.class);

            assertThat(() -> instance.produce(classRelationFor(this), new Object()),
                throwsException(TestError.class, "test error"));
        }

        @Test
        void methodBasedContentProducerFor_handles_Exception() throws Exception {
            final ContentProducer<Object> instance = methodBasedContentProducerFor(TestAnnotation.class, "throwingExceptionGeneratorMethod", Object.class);

            assertThat(() -> instance.produce(classRelationFor(this), new Object()),
                throwsException(RuntimeException.class, "test exception"));
        }

        @Test
        void methodBasedContentProducerFor_handles_IOException() throws Exception {
            final ContentProducer<Object> instance = methodBasedContentProducerFor(TestAnnotation.class, "throwingIOExceptionGeneratorMethod", Object.class);

            assertThat(() -> instance.produce(classRelationFor(this), new Object()),
                throwsException(UncheckedIOException.class, "test io exception"));
        }

        @Test
        void methodBasedContentProducerFor_handles_RuntimeException() throws Exception {
            final ContentProducer<Object> instance = methodBasedContentProducerFor(TestAnnotation.class, "throwingRuntimeExceptionGeneratorMethod", Object.class);

            assertThat(() -> instance.produce(classRelationFor(this), new Object()),
                throwsException(TestRuntimeException.class, "test runtime exception"));
        }

        @Test
        void generatorMethodFor_find_for_ObjectRelation() throws Exception {
            final Relation<?> relation = objectRelationFor(this);

            final Method actual = generatorMethodFor(TestAnnotation.class, relation, "instanceGeneratorMethod", AtomicLong.class);

            assertThat(actual.getName(), isEqualTo("instanceGeneratorMethod"));
            assertThat(actual.getDeclaringClass(), isEqualTo(getClass()));
        }

        @Test
        void generatorMethodFor_find_for_ClassRelation() throws Exception {
            final Relation<?> relation = classRelationFor(this);

            final Method actual = generatorMethodFor(TestAnnotation.class, relation, "staticGeneratorMethod", AtomicLong.class);

            assertThat(actual.getName(), isEqualTo("staticGeneratorMethod"));
            assertThat(actual.getDeclaringClass(), isEqualTo(getClass()));
        }

        @Test
        void generatorMethodFor_fails_if_instanceMethod_but_only_ClassRelation_provided() throws Exception {
            final Relation<?> relation = classRelationFor(this);

            assertThat(() -> generatorMethodFor(TestAnnotation.class, relation, "instanceGeneratorMethod", AtomicLong.class),
                throwsException(IllegalArgumentException.class, ".*which is not static but needs to be static.*"));
        }

        @Test
        void generatorMethodFor_fails_if_method_does_not_exists() throws Exception {
            final Relation<?> relation = classRelationFor(this);

            assertThat(() -> generatorMethodFor(TestAnnotation.class, relation, "foo", AtomicLong.class),
                throwsException(IllegalArgumentException.class, ".*but it does not exist.*"));
        }

        @Nonnull
        private static TestAnnotation givenAnnotationFor(@Nonnull String testFieldName) throws Exception {
            final Field field = TestFields.class.getDeclaredField(testFieldName);
            return Optional.ofNullable(field.getAnnotation(TestAnnotation.class))
                .orElseThrow(() -> new IllegalArgumentException("Annotation missing."));
        }

        @Nonnull
        private static WithoutTemporaryPathAnnotation givenBrokenAnnotationFor(@Nonnull String testFieldName) throws Exception {
            final Field field = TestFields.class.getDeclaredField(testFieldName);
            return Optional.ofNullable(field.getAnnotation(WithoutTemporaryPathAnnotation.class))
                .orElseThrow(() -> new IllegalArgumentException("Annotation missing."));
        }

        private void instanceGeneratorMethod(AtomicLong argument) throws Exception {
            argument.set(111);
        }

        private static void staticGeneratorMethod(AtomicLong argument) throws Exception {
            argument.set(222);
        }

        private static void throwingErrorGeneratorMethod(Object argument) throws Exception {
            throw new TestError("test error");
        }

        private static void throwingExceptionGeneratorMethod(Object argument) throws Exception {
            throw new TestException("test exception");
        }

        private static void throwingIOExceptionGeneratorMethod(Object argument) throws Exception {
            throw new TestIOException("test io exception");
        }

        private static void throwingRuntimeExceptionGeneratorMethod(Object argument) throws Exception {
            throw new TestRuntimeException("test runtime exception");
        }

        @Target({FIELD})
        @Retention(RUNTIME)
        private static @interface WithoutTemporaryPathAnnotation {}

        @Target({FIELD})
        @Retention(RUNTIME)
        @TemporaryPath(provider = ProviderImpl.class)
        private static @interface TestAnnotation {
            String value();
        }

        @Target({FIELD})
        @Retention(RUNTIME)
        @TemporaryPath(provider = ProviderImpl.class)
        private static @interface OtherTestAnnotation {}

        private static class ProviderImpl implements Provider<TestAnnotation> {

            @Nonnull
            @Override
            public Path provide(@Nonnull TestAnnotation forAnnotation, @Nonnull Relation<?> relation, @Nonnull TemporaryPathBroker using) throws Exception {
                return Paths.get(forAnnotation.value());
            }

        }

        @SuppressWarnings("unused")
        private static class TestFields {
            private Path fieldWithNoAnnotation;
            @TestAnnotation("1")
            private Path workingField;
            @WithoutTemporaryPathAnnotation()
            private Path withoutTemporaryPathAnnotationField;
            @TestAnnotation("1")
            @OtherTestAnnotation
            private Path ambiguousField;
        }

        private static class TestError extends Error {
            public TestError(String message) {
                super(message);
            }
        }

        private static class TestException extends Exception {
            public TestException(String message) {
                super(message);
            }
        }

        private static class TestIOException extends IOException {
            public TestIOException(String message) {
                super(message);
            }
        }

        private static class TestRuntimeException extends RuntimeException {
            public TestRuntimeException(String message) {
                super(message);
            }
        }


    }

}
