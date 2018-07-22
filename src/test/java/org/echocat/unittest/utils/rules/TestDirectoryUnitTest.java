package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.extensions.TemporaryDirectory;
import org.echocat.unittest.utils.extensions.TemporaryPaths;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.toSet;
import static org.echocat.unittest.utils.matchers.HasSize.hasLengthOf;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSameInstance;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.rules.TestDirectory.NOOP_CONTENT_PRODUCER;
import static org.junit.Assert.assertThat;
import static org.junit.runner.Description.createTestDescription;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("deprecation")
@ExtendWith(TemporaryPaths.class)
public class TestDirectoryUnitTest {

    private static final ContentProducer<Path> PRODUCER = (relation, to) -> {};

    @Test
    void roundTrip_works_as_expected() throws Throwable {
        final TestDirectory instance = new TestDirectory("foo", (relation, to) -> createFile(to.resolve("foo.txt")));
        final Statement statement = mock(Statement.class);
        final Description description = createTestDescription("foo.bar.Test", "test");
        doAnswer(call -> {
            assertThat(isDirectory(instance), isEqualTo(true));
            assertThat(isRegularFile(instance.resolve("foo.txt")), isEqualTo(true));
            assertThat(childrenOf(instance), hasLengthOf(1));
            return null;
        }).when(statement).evaluate();

        instance.apply(statement, description).evaluate();

        verify(statement, times(1)).evaluate();
    }

    @Test
    void NOOP_CONTENT_PRODUCER_is_doing_nothing(@TemporaryDirectory Path root) throws Exception {
        NOOP_CONTENT_PRODUCER.produce(classRelationFor(this), root);

        final Set<Path> paths = childrenOf(root);
        assertThat(paths, hasLengthOf(0));
    }

    @Test
    void constructor_with_nothing() throws Exception {
        final TestDirectory actual = new TestDirectory();

        assertThat(actual.name(), isEqualTo("test.directory"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor() throws Exception {
        final TestDirectory actual = new TestDirectory("foo.ext", PRODUCER);

        assertThat(actual.name(), isEqualTo("foo.ext"));
        assertThat(actual.contentProducer(), isSameInstance(PRODUCER));
    }

    @Test
    void constructor_with_only_fileName() throws Exception {
        final TestDirectory actual = new TestDirectory("foo.ext");

        assertThat(actual.name(), isEqualTo("foo.ext"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor_with_null_producer() throws Exception {
        final TestDirectory actual = new TestDirectory("foo.ext", null);

        assertThat(actual.name(), isEqualTo("foo.ext"));
        assertThat(actual.contentProducer(), isEqualTo(NOOP_CONTENT_PRODUCER));
    }

    @Test
    void constructor_with_only_producer() throws Exception {
        final TestDirectory actual = new TestDirectory(PRODUCER);

        assertThat(actual.name(), isEqualTo("test"));
        assertThat(actual.contentProducer(), isSameInstance(PRODUCER));
    }

    @Nonnull
    private static Set<Path> childrenOf(@Nonnull Path what) throws IOException {
        try (final Stream<Path> paths = walk(what)) {
            return paths
                .filter(candidate -> !candidate.equals(what))
                .collect(toSet());
        }
    }

}