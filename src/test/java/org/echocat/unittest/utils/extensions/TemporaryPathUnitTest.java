package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryPath.Provider;
import org.echocat.unittest.utils.extensions.TemporaryPath.Utils;
import org.echocat.unittest.utils.matchers.OptionalMatchers;
import org.echocat.unittest.utils.matchers.WhereValueOf;
import org.echocat.unittest.utils.nio.Relation;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isAbsent;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.whereContentMatches;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.matchers.WhereValueOf.whereValueOf;
import static org.echocat.unittest.utils.nio.TemporaryPathBroker.temporaryPathBrokerFor;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemporaryPathUnitTest {

    static class UtilsUnitTest {

        @Test
        void provideCallsProviderAsExpected() throws Exception {
            try (final TemporaryPathBroker broker = temporaryPathBrokerFor("test")) {
                final TestAnnotation annotation = givenAnnotationFor("workingField");

                final Path actual = Utils.provide(annotation, Relation.classRelationFor(TestFields.class), broker);

                assertThat(actual, isEqualTo(Paths.get("1")));
            }
        }

        @Test
        void provideWillFailIfTemporaryPathAnnotationIsMissing() throws Exception {
            try (final TemporaryPathBroker broker = temporaryPathBrokerFor("test")) {
                final WithoutTemporaryPathAnnotation annotation = givenBrokenAnnotationFor("withoutTemporaryPathAnnotationField");

                assertThat(() -> Utils.provide(annotation, Relation.classRelationFor(TestFields.class), broker),
                    throwsException(IllegalStateException.class, "Annotation @.*WithoutTemporaryPathAnnotation is not annotated.*"));
            }
        }

        @Test
        void findTemporaryPathEnabledAnnotationWorksAsExpected() throws Exception {
            final Field field = TestFields.class.getDeclaredField("workingField");

            final Optional<Annotation> actual = Utils.findTemporaryPathEnabledAnnotation(field);

            assertThat(actual, whereContentMatches(whereValueOf(Annotation::annotationType, "type", isEqualTo(TestAnnotation.class))));
        }

        @Test
        void findTemporaryPathEnabledAnnotationCannotFindAnnotations() throws Exception {
            final Field field = TestFields.class.getDeclaredField("fieldWithNoAnnotation");

            final Optional<Annotation> actual = Utils.findTemporaryPathEnabledAnnotation(field);

            assertThat(actual, isAbsent());
        }

        @Test
        void findTemporaryPathEnabledAnnotationWillFailOnAmbiguousAnnotations() throws Exception {
            final Field field = TestFields.class.getDeclaredField("ambiguousField");

            assertThat(() -> Utils.findTemporaryPathEnabledAnnotation(field),
                throwsException(IllegalArgumentException.class, "The element .+ambiguousField is annotated with more than one.+"));
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


    }

}
