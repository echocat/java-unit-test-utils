package org.echocat.unittest.utils.rules;

import org.echocat.unittest.utils.extensions.TemporaryDirectory;
import org.echocat.unittest.utils.extensions.TemporaryPaths;
import org.echocat.unittest.utils.nio.TemporaryPathBroker;
import org.echocat.unittest.utils.nio.TemporaryPathBroker.ContentProducer;
import org.echocat.unittest.utils.nio.Interceptor;
import org.echocat.unittest.utils.nio.EventType;
import org.echocat.unittest.utils.nio.WrappedPath;
import org.echocat.unittest.utils.utils.ExceptionUtils.Execution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.isRegularFile;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.nio.TemporaryPathBrokerTestUtils.temporaryPathBrokerFor;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.delete;
import static org.echocat.unittest.utils.nio.WrappedPath.create;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"rawtypes", "AccessingNonPublicFieldOfAnotherObject", "EqualsWithItself"})
@ExtendWith(TemporaryPaths.class)
public class TemporaryDirectoryBasedRuleSupportUnitTest {

    private static final ContentProducer<OutputStream> PRODUCER = (relation, to) -> {
    };

    @Test
    void wrapped_is_set_while_apply() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance();
        final TestStatement statement = givenStatement(() ->
            assertThat(isRegularFile(instance.wrapped()), isEqualTo(true))
        );
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void equals_works_as_expected() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance();
        final TestStatement statement = givenStatement(() -> {
            assertThat(instance.equals(instance), isEqualTo(true));
            assertThat(instance.equals(instance.wrapped()), isEqualTo(true));
            assertThat(instance.equals(create(instance.wrapped())), isEqualTo(true));
            assertThat(instance.equals(new Object()), isEqualTo(false));
        });
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void compareTo_works_as_expected() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance();
        final TestStatement statement = givenStatement(() -> {
            assertThat(instance.compareTo(instance), isEqualTo(0));
            assertThat(instance.compareTo(instance.wrapped()), isEqualTo(0));
            assertThat(instance.compareTo(create(instance.wrapped())), isEqualTo(0));
        });
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void hashCode_works_as_expected() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance();
        final TestStatement statement = givenStatement(() ->
            assertThat(instance.hashCode(), isEqualTo(instance.wrapped().hashCode()))
        );
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void toString_works_as_expected() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance();
        final TestStatement statement = givenStatement(() ->
            assertThat(instance.toString(), isEqualTo(instance.wrapped().toString()))
        );
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void apply_respects_errors_on_cleanup(@TemporaryDirectory Path root) throws Throwable {
        final WrappedPath path = WrappedPath.create(root, givenInterceptor());
        final TemporaryDirectoryBasedRuleSupport instance = givenInstanceFor(path);
        final TestStatement statement = givenStatement();
        final Description description = givenDescription();

        assertThat(() -> instance.apply(statement, description).evaluate(),
            throwsException(UncheckedIOException.class, "TEST"));

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void apply_ignores_errors_on_cleanup(@TemporaryDirectory Path root) throws Throwable {
        final WrappedPath path = WrappedPath.create(root, givenInterceptor());
        final TemporaryDirectoryBasedRuleSupport instance = givenInstanceFor(path)
            .setFailOnProblemsWhileCleanup(false);
        final TestStatement statement = givenStatement();
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Test
    void apply_works_interceptor() throws Throwable {
        final TemporaryDirectoryBasedRuleSupport instance = givenInstance()
            .setInterceptor((event, result) -> {
                final EventType type = event.type();
                if (type.equals(delete)) {
                    throw new IOException("TEST");
                }
                return empty();
            });
        final TestStatement statement = givenStatement(() ->
            assertThat(() -> delete(instance)
                , throwsException(IOException.class, "TEST"))
        );
        final Description description = givenDescription();

        instance.apply(statement, description).evaluate();

        assertThat(statement.numberOfCalls.get(), isEqualTo(1));
    }

    @Nonnull
    private static TemporaryDirectoryBasedRuleSupport givenInstanceFor(@Nonnull Path path) {
        return new TemporaryDirectoryBasedRuleSupport() {
            @Override
            protected Path evaluatePath(@Nonnull Statement base, @Nonnull Description description, @Nonnull TemporaryPathBroker broker) throws Throwable {
                return broker.newFile("foo", classRelationFor(this), PRODUCER);
            }

            @Nonnull
            @Override
            protected TemporaryPathBroker newBrokerFor(@Nonnull Description description) {
                return temporaryPathBrokerFor(path);
            }
        };
    }

    @Nonnull
    private static TemporaryDirectoryBasedRuleSupport givenInstance() {
        return new TemporaryDirectoryBasedRuleSupport() {
            @Override
            protected Path evaluatePath(@Nonnull Statement base, @Nonnull Description description, @Nonnull TemporaryPathBroker broker) throws Throwable {
                return broker.newFile("foo", classRelationFor(this), PRODUCER);
            }
        };
    }

    @Nonnull
    private static Interceptor givenInterceptor() {
        return (event, result) -> {
            if (delete.equals(event.type())) {
                throw new IOException("TEST");
            }
            return empty();
        };
    }

    @Nonnull
    private static Description givenDescription() {
        return Description.createTestDescription(TemporaryDirectoryBasedRuleSupportUnitTest.class, "foo");
    }

    @Nonnull
    private static TestStatement givenStatement() {
        return givenStatement(null);
    }

    @Nonnull
    private static TestStatement givenStatement(@Nullable Execution<Throwable> execution) {
        return new TestStatement(execution);
    }

    private static final class TestStatement extends Statement {
        @Nonnull
        private final AtomicInteger numberOfCalls = new AtomicInteger();
        @Nonnull
        private final Optional<Execution<Throwable>> execution;

        private TestStatement(@Nullable Execution<Throwable> execution) {
            this.execution = ofNullable(execution);
        }

        @Override
        public void evaluate() throws Throwable {
            numberOfCalls.incrementAndGet();
            if (this.execution.isPresent()) {
                this.execution.get().execute();
            }
        }
    }

}