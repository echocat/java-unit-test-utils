package org.echocat.unittest.utils.extensions;

import org.echocat.unittest.utils.extensions.TemporaryFile.Provider;
import org.echocat.unittest.utils.extensions.TemporaryFile.Root;
import org.echocat.unittest.utils.extensions.TemporaryFile.TestClass;
import org.echocat.unittest.utils.extensions.subpackage.Something;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.util.Collections.singletonList;
import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TemporaryPaths.class)
public class TemporaryFileUnitTest {

    @TemporaryFile
    private Path instanceField1;
    @TemporaryFile
    private Path instanceField2;
    @TemporaryFile(usingGeneratorMethod = "instanceGeneratorMethod")
    private Path instanceFieldWithGenerator;
    @TemporaryFile(usingGeneratorMethod = "staticGeneratorMethod")
    private Path instanceFieldStaticWithGenerator;

    @TemporaryFile
    private static Path classField1;
    @TemporaryFile
    private static Path classField2;
    @TemporaryFile(usingGeneratorMethod = "staticGeneratorMethod")
    private static Path staticFieldWithGenerator;

    private void instanceGeneratorMethod(@Nonnull OutputStream to) throws Exception {
        to.write("instance-abc".getBytes(UTF_8));
    }

    private static void staticGeneratorMethod(@Nonnull OutputStream to) throws Exception {
        to.write("static-abc".getBytes(UTF_8));
    }

    @Test
    void injectionWithOneParameterWorksAsExpected(@TemporaryFile Path parameter1) throws Exception {
        assertThat(exists(parameter1), isEqualTo(true));
        assertThat(isRegularFile(parameter1), isEqualTo(true));
        assertThat(size(parameter1), isEqualTo(0L));
    }

    @Test
    void injectionWithTwoParametersWorksAsExpected(@TemporaryFile Path parameter1, @TemporaryFile Path parameter2) throws Exception {
        assertThat(exists(parameter1), isEqualTo(true));
        assertThat(isRegularFile(parameter1), isEqualTo(true));
        assertThat(size(parameter1), isEqualTo(0L));

        assertThat(exists(parameter2), isEqualTo(true));
        assertThat(isRegularFile(parameter2), isEqualTo(true));
        assertThat(size(parameter2), isEqualTo(0L));

        assertThat(parameter1, isNot(equalTo(parameter2)));
        assertThat(parameter1.getParent(), isNot(equalTo(parameter2.getParent())));
        assertThat(parameter1.getParent().getParent(), isEqualTo(parameter2.getParent().getParent()));
    }


    @Test
    void injectionWithInstanceFieldsWorksAsExpected() throws Exception {
        assertThat(exists(instanceField1), isEqualTo(true));
        assertThat(isRegularFile(instanceField1), isEqualTo(true));
        assertThat(size(instanceField1), isEqualTo(0L));

        assertThat(exists(instanceField2), isEqualTo(true));
        assertThat(isRegularFile(instanceField2), isEqualTo(true));
        assertThat(size(instanceField2), isEqualTo(0L));

        assertThat(instanceField1, isNot(equalTo(instanceField2)));
        assertThat(instanceField1.getParent(), isNot(equalTo(instanceField2.getParent())));
        assertThat(instanceField1.getParent().getParent(), isEqualTo(instanceField2.getParent().getParent()));
    }

    @Test
    void injectionWithClassFieldsWorksAsExpected() throws Exception {
        assertThat(exists(classField1), isEqualTo(true));
        assertThat(isRegularFile(classField1), isEqualTo(true));
        assertThat(size(classField1), isEqualTo(0L));

        assertThat(exists(classField2), isEqualTo(true));
        assertThat(isRegularFile(classField2), isEqualTo(true));
        assertThat(size(classField2), isEqualTo(0L));

        assertThat(classField1, isNot(equalTo(classField2)));
        assertThat(classField1.getParent(), isNot(equalTo(classField2.getParent())));
        assertThat(classField1.getParent().getParent(), isEqualTo(classField2.getParent().getParent()));
    }

    @Test
    void ofNameWorksAsExpected(@TemporaryFile(ofName = "foobar") Path path) throws Exception {
        assertThat(path.getFileName().toString(), isEqualTo("foobar"));
    }

    @Test
    void defaultNameWillUsedIfOfNameIsAbsent(@TemporaryFile Path path) throws Exception {
        assertThat(path.getFileName().toString(), isEqualTo("test"));
    }

    @Test
    void fromClasspathWorksAsExpected(@TemporaryFile(fromClasspath = "/testFile.txt") Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("org.echocat.unittest.utils.extensions")));
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Test
    void fromClasspathWithRelationToOfTestClassWorksAsExpected(@TemporaryFile(fromClasspath = "//testFile.txt", relativeTo = TestClass.class) Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("org.echocat.unittest.utils.extensions")));
    }

    @Test
    void fromClasspathWithRelationToOfSomeClassWorksAsExpected(@TemporaryFile(fromClasspath = "/testFile.txt", relativeTo = Something.class) Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("org.echocat.unittest.utils.extensions.subpackage")));
    }

    @Test
    void fromClasspathWithRelationToOfRootWorksAsExpected(@TemporaryFile(fromClasspath = "/testFile.txt", relativeTo = Root.class) Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("ROOT")));
    }

    @Test
    void withContentWorksAsExpected(@TemporaryFile(withContent = "string content") Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("string content")));
    }

    @Test
    void withBinaryContentWorksAsExpected(@TemporaryFile(withBinaryContent = {6, 6, 6}) Path path) throws Exception {
        assertThat(readAllBytes(path), isEqualTo(new byte[]{6, 6, 6}));
    }

    @Test
    void withRandomContentOfLengthWorksAsExpected(@TemporaryFile(withRandomContentOfLength = 666) Path path) throws Exception {
        assertThat(size(path), isEqualTo(666L));
    }

    @Test
    void usingInstanceGeneratorMethodForMethodParameterInjectionWorksAsExpected(@TemporaryFile(usingGeneratorMethod = "instanceGeneratorMethod") Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("instance-abc")));
    }

    @Test
    void usingStaticGeneratorMethodForMethodParameterInjectionWorksAsExpected(@TemporaryFile(usingGeneratorMethod = "staticGeneratorMethod") Path path) throws Exception {
        assertThat(readAllLines(path), isEqualTo(singletonList("static-abc")));
    }

    @Test
    void usingInstanceGeneratorMethodForInstanceFieldInjectionWorksAsExpected() throws Exception {
        assertThat(readAllLines(instanceFieldWithGenerator), isEqualTo(singletonList("instance-abc")));
    }

    @Test
    void usingStaticGeneratorMethodForInstanceFieldInjectionWorksAsExpected() throws Exception {
        assertThat(readAllLines(instanceFieldStaticWithGenerator), isEqualTo(singletonList("static-abc")));
    }

    @Test
    void usingStaticGeneratorMethodForStaticFieldInjectionWorksAsExpected() throws Exception {
        assertThat(readAllLines(staticFieldWithGenerator), isEqualTo(singletonList("static-abc")));
    }

    @Test
    void fromClassPathAndWithContentDoesNotWorkTogether() throws Exception {
        final Provider provider = givenProvider();
        final TemporaryFile instance = givenInstanceFor("fromClassPathAndWithContent");

        assertThat(() -> provider.contentProducerFor(instance),
            throwsException(IllegalArgumentException.class, "The definition of given @TemporaryFile is ambiguous - it leads to more than one way to generate the content."));
    }

    @Test
    void fromClassPathAndWithBinaryContentDoesNotWorkTogether() throws Exception {
        final Provider provider = givenProvider();
        final TemporaryFile instance = givenInstanceFor("fromClassPathAndWithBinaryContent");

        assertThat(() -> provider.contentProducerFor(instance),
            throwsException(IllegalArgumentException.class, "The definition of given @TemporaryFile is ambiguous - it leads to more than one way to generate the content."));
    }

    @Test
    void withContentAndWithBinaryContentDoesNotWorkTogether() throws Exception {
        final Provider provider = givenProvider();
        final TemporaryFile instance = givenInstanceFor("withContentAndWithBinaryContent");

        assertThat(() -> provider.contentProducerFor(instance),
            throwsException(IllegalArgumentException.class, "The definition of given @TemporaryFile is ambiguous - it leads to more than one way to generate the content."));
    }

    @Nonnull
    private static TemporaryFile givenInstanceFor(@Nonnull String testFieldName) throws Exception {
        final Field field = TestFields.class.getDeclaredField(testFieldName);
        return Optional.ofNullable(field.getAnnotation(TemporaryFile.class))
            .orElseThrow(() -> new IllegalArgumentException("Annotation missing."));
    }

    @Nonnull
    private static TemporaryFile.Provider givenProvider() {
        return new Provider();
    }

    private static class TestFields {
        @TemporaryFile(fromClasspath = "testFile.txt", withContent = "content")
        private Path fromClassPathAndWithContent;
        @TemporaryFile(fromClasspath = "testFile.txt", withBinaryContent = {6})
        private Path fromClassPathAndWithBinaryContent;
        @TemporaryFile(withContent = "content", withBinaryContent = {6})
        private Path withContentAndWithBinaryContent;
    }

}
