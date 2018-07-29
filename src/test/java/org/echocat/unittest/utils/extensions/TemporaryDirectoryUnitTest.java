package org.echocat.unittest.utils.extensions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.Files.*;
import static org.echocat.unittest.utils.TestUtils.childrenOf;
import static org.echocat.unittest.utils.TestUtils.numberOfChildrenOf;
import static org.echocat.unittest.utils.matchers.HasSize.hasLengthOf;
import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.echocat.unittest.utils.matchers.IterableMatchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TemporaryPaths.class)
public class TemporaryDirectoryUnitTest {

    @TemporaryDirectory
    private Path instanceField1;
    @TemporaryDirectory
    private Path instanceField2;
    @TemporaryDirectory(usingGeneratorMethod = "instanceGeneratorMethod")
    private Path instanceFieldWithGenerator;
    @TemporaryDirectory(usingGeneratorMethod = "staticGeneratorMethod")
    private Path instanceFieldStaticWithGenerator;

    @TemporaryDirectory
    private static Path classField1;
    @TemporaryDirectory
    private static Path classField2;
    @TemporaryDirectory(usingGeneratorMethod = "staticGeneratorMethod")
    private static Path staticFieldWithGenerator;

    private void instanceGeneratorMethod(@Nonnull Path to) throws Exception {
        createFile(to.resolve("instance-foo"));
        createFile(to.resolve("instance-bar"));
    }

    private static void staticGeneratorMethod(@Nonnull Path to) throws Exception {
        createFile(to.resolve("static-foo"));
        createFile(to.resolve("static-bar"));
    }

    @Test
    void injectionWithOneParameterWorksAsExpected(@TemporaryDirectory Path parameter1) throws Exception {
        assertThat(exists(parameter1), isEqualTo(true));
        assertThat(isDirectory(parameter1), isEqualTo(true));
        assertThat(numberOfChildrenOf(parameter1), isEqualTo(0L));
    }

    @Test
    void injectionWithTwoParametersWorksAsExpected(@TemporaryDirectory Path parameter1, @TemporaryDirectory Path parameter2) throws Exception {
        assertThat(exists(parameter1), isEqualTo(true));
        assertThat(isDirectory(parameter1), isEqualTo(true));
        assertThat(numberOfChildrenOf(parameter1), isEqualTo(0L));

        assertThat(exists(parameter2), isEqualTo(true));
        assertThat(isDirectory(parameter2), isEqualTo(true));
        assertThat(numberOfChildrenOf(parameter2), isEqualTo(0L));

        assertThat(parameter1, isNot(equalTo(parameter2)));
        assertThat(parameter1.getParent(), isNot(equalTo(parameter2.getParent())));
        assertThat(parameter1.getParent().getParent(), isEqualTo(parameter2.getParent().getParent()));
    }


    @Test
    void injectionWithInstanceFieldsWorksAsExpected() throws Exception {
        assertThat(exists(instanceField1), isEqualTo(true));
        assertThat(isDirectory(instanceField1), isEqualTo(true));
        assertThat(numberOfChildrenOf(instanceField1), isEqualTo(0L));

        assertThat(exists(instanceField2), isEqualTo(true));
        assertThat(isDirectory(instanceField2), isEqualTo(true));
        assertThat(numberOfChildrenOf(instanceField2), isEqualTo(0L));

        assertThat(instanceField1, isNot(equalTo(instanceField2)));
        assertThat(instanceField1.getParent(), isNot(equalTo(instanceField2.getParent())));
        assertThat(instanceField1.getParent().getParent(), isEqualTo(instanceField2.getParent().getParent()));
    }

    @Test
    void injectionWithClassFieldsWorksAsExpected() throws Exception {
        assertThat(exists(classField1), isEqualTo(true));
        assertThat(isDirectory(classField1), isEqualTo(true));
        assertThat(numberOfChildrenOf(classField1), isEqualTo(0L));

        assertThat(exists(classField2), isEqualTo(true));
        assertThat(isDirectory(classField2), isEqualTo(true));
        assertThat(numberOfChildrenOf(classField2), isEqualTo(0L));

        assertThat(classField1, isNot(equalTo(classField2)));
        assertThat(classField1.getParent(), isNot(equalTo(classField2.getParent())));
        assertThat(classField1.getParent().getParent(), isEqualTo(classField2.getParent().getParent()));
    }

    @Test
    void ofNameWorksAsExpected(@TemporaryDirectory(ofName = "foobar") Path path) throws Exception {
        assertThat(path.getFileName().toString(), isEqualTo("foobar"));
    }

    @Test
    void defaultNameWillUsedIfOfNameIsAbsent(@TemporaryDirectory Path path) throws Exception {
        assertThat(path.getFileName().toString(), isEqualTo("test"));
    }

    @Test
    void usingInstanceGeneratorMethodForMethodParameterInjectionWorksAsExpected(@TemporaryDirectory(usingGeneratorMethod = "instanceGeneratorMethod") Path path) throws Exception {
        final Set<String> children = childrenOf(path);
        assertThat(children, hasLengthOf(2));
        assertThat(children, contains("instance-foo"));
        assertThat(children, contains("instance-bar"));
    }

    @Test
    void usingStaticGeneratorMethodForMethodParameterInjectionWorksAsExpected(@TemporaryDirectory(usingGeneratorMethod = "staticGeneratorMethod") Path path) throws Exception {
        final Set<String> children = childrenOf(path);
        assertThat(children, hasLengthOf(2));
        assertThat(children, contains("static-foo"));
        assertThat(children, contains("static-bar"));
    }

    @Test
    void usingInstanceGeneratorMethodForInstanceFieldInjectionWorksAsExpected() throws Exception {
        final Set<String> children = childrenOf(instanceFieldWithGenerator);
        assertThat(children, hasLengthOf(2));
        assertThat(children, contains("instance-foo"));
        assertThat(children, contains("instance-bar"));
    }

    @Test
    void usingStaticGeneratorMethodForInstanceFieldInjectionWorksAsExpected() throws Exception {
        final Set<String> children = childrenOf(instanceFieldStaticWithGenerator);
        assertThat(children, hasLengthOf(2));
        assertThat(children, contains("static-foo"));
        assertThat(children, contains("static-bar"));
    }

    @Test
    void usingStaticGeneratorMethodForStaticFieldInjectionWorksAsExpected() throws Exception {
        final Set<String> children = childrenOf(staticFieldWithGenerator);
        assertThat(children, hasLengthOf(2));
        assertThat(children, contains("static-foo"));
        assertThat(children, contains("static-bar"));
    }

}
