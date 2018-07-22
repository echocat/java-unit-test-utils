package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.hamcrest.Condition.Step;
import org.junit.jupiter.api.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.swing.plaf.nimbus.State;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.rules.TestFile.NOOP_CONTENT_PRODUCER;
import static org.echocat.unittest.utils.rules.TestFile.fromClasspath;
import static org.echocat.unittest.utils.rules.TestFile.withContent;
import static org.echocat.unittest.utils.rules.TestFile.withGeneratedContent;
import static org.junit.Assert.assertThat;
import static org.junit.runner.Description.createTestDescription;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("deprecation")
class TestFileUnitTest {

    private static final byte[] BYTES = {6, 6, 6};
    private static final String STRING = "666";

    @Test
    void roundTrip_works_as_expected() throws Throwable {
        final TestFile instance = new TestFile("foo", (relation, to) -> to.write(BYTES));
        final Statement statement = mock(Statement.class);
        final Description description = createTestDescription("foo.bar.Test", "test");
        doAnswer(call -> {
            assertThat(Files.isRegularFile(instance), isEqualTo(true));
            assertThat(Files.readAllBytes(instance), isEqualTo(BYTES));
            return null;
        }).when(statement).evaluate();

        instance.apply(statement, description).evaluate();

        verify(statement, times(1)).evaluate();
    }

    @Test
    void NOOP_CONTENT_PRODUCER_is_doing_nothing() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        NOOP_CONTENT_PRODUCER.produce(classRelationFor(this), os);

        assertThat(os.size(), isEqualTo(0));
    }

    @Test
    void constructor_with_nothing() throws Exception {
        final TestFile actual = new TestFile();

        assertThat(actual.name(), isEqualTo("test.file"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor_with_only_fileName() throws Exception {
        final TestFile actual = new TestFile("foo.ext");

        assertThat(actual.name(), isEqualTo("foo.ext"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor_with_null_producer() throws Exception {
        final TestFile actual = new TestFile("foo.ext", (ContentProducer<OutputStream>) null);

        assertThat(actual.name(), isEqualTo("foo.ext"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor_with_bytes() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        final TestFile actual = new TestFile("foo.ext", BYTES);

        assertThat(actual.name(), isEqualTo("foo.ext"));
        actual.contentProducer().produce(classRelationFor(this), os);
        assertThat(os.toByteArray(), isEqualTo(BYTES));
    }

    @Test
    void constructor_with_string() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        final TestFile actual = new TestFile("foo.ext", STRING);

        assertThat(actual.name(), isEqualTo("foo.ext"));
        actual.contentProducer().produce(classRelationFor(this), os);
        assertThat(os.toString(), isEqualTo(STRING));
    }

    @Test
    void withContent_consuming_String_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        withContent(STRING).produce(classRelationFor(this), os);

        assertThat(os.toString(), isEqualTo(STRING));
    }

    @Test
    void withContent_consuming_bytes_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        withContent(BYTES).produce(classRelationFor(this), os);

        assertThat(os.toByteArray(), isEqualTo(BYTES));
    }

    @Test
    void withGeneratedContent_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final int numberOfBytes = (4096 * 6) - 666;

        withGeneratedContent(numberOfBytes).produce(classRelationFor(this), os);

        assertThat(os.size(), isEqualTo(numberOfBytes));
    }

    @Test
    void fromClasspath_with_Class_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        fromClasspath("testFile.txt", TestFileUnitTest.class).produce(classRelationFor(this), os);

        assertThat(os.toString(), isEqualTo(TestFileUnitTest.class.getPackage().getName()));
    }

    @Test
    void fromClasspath_with_ClassPath_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        fromClasspath("testFile.txt", TestFileUnitTest.class.getClassLoader()).produce(classRelationFor(this), os);

        assertThat(os.toString(), isEqualTo("ROOT"));
    }

    @Test
    void fromClasspath_works_as_expected() throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        fromClasspath("testFile.txt").produce(classRelationFor(this), os);

        assertThat(os.toString(), isEqualTo("ROOT"));
    }

}