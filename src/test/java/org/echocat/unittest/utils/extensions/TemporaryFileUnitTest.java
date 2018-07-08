package org.echocat.unittest.utils.extensions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.TestUtils.numberOfChildrenOf;
import static org.echocat.unittest.utils.matchers.IsEqualTo.equalTo;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(TemporaryPaths.class)
public class TemporaryFileUnitTest {

    @TemporaryFile
    private Path instanceField1;
    @TemporaryFile
    private Path instanceField2;

    @TemporaryFile
    private static Path classField1;
    @TemporaryFile
    private static Path classField2;

    private void generatorMethod(@Nonnull OutputStream to) throws Exception {
        to.write("abc".getBytes(UTF_8));
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
    void usingGeneratorMethodWorksAsExpected(@TemporaryFile(usingGeneratorMethod = "generatorMethod") Path path) throws Exception {
        assertThat(size(path), isEqualTo(3L));
        assertThat(Files.readAllLines(path), isEqualTo(asList("abc")));
    }

}
