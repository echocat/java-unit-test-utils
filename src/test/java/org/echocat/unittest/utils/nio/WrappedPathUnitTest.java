package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.WrappedPath.Default;
import org.echocat.unittest.utils.nio.WrappedPath.Support;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchService;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsInstanceOf.isInstanceOf;
import static org.echocat.unittest.utils.matchers.IsNot.isNot;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSame;
import static org.echocat.unittest.utils.matchers.IsSameAs.isSameAs;
import static org.echocat.unittest.utils.matchers.IsSameAs.sameAs;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.isAbsent;
import static org.echocat.unittest.utils.matchers.OptionalMatchers.whereContentMatches;
import static org.echocat.unittest.utils.nio.WrappedPath.wrap;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WrappedPathUnitTest {

    @Test
    void wrap_without_Interceptor() throws Exception {
        final Path mockPath = givenMockPath();

        final WrappedPath actual = wrap(mockPath);

        assertThat(actual, isInstanceOf(WrappedPath.class));
        assertThat(actual.wrapped(), isSameAs(mockPath));
        assertThat(actual.interceptor(), isAbsent());
    }

    @Test
    void wrap_with_Interceptor() throws Exception {
        final Path mockPath = givenMockPath();
        final Interceptor interceptor = (event, result) -> Optional.empty();

        final WrappedPath actual = wrap(mockPath, interceptor);

        assertThat(actual, isInstanceOf(WrappedPath.class));
        assertThat(actual.wrapped(), isSameAs(mockPath));
        assertThat(actual.interceptor(), whereContentMatches(isSame(interceptor)));
    }

    @Test
    void wrap_will_not_wrap_on_sameInstance_and_Interceptor() throws Exception {
        final Path mockPath = givenMockPath();
        final Interceptor interceptor = (event, result) -> Optional.empty();
        final WrappedPath wrapped = wrap(mockPath, interceptor);

        final WrappedPath actual = wrap(wrapped, interceptor);

        assertThat(actual, isSameAs(wrapped));
    }

    @Test
    void wrap_will_wrap_on_sameInstance_and_but_different_Interceptor() throws Exception {
        final Path mockPath = givenMockPath();
        final Interceptor interceptor1 = (event, result) -> Optional.empty();
        final WrappedPath wrapped = wrap(mockPath, interceptor1);

        final Interceptor interceptor2 = (event, result) -> Optional.empty();
        final WrappedPath actual = wrap(wrapped, interceptor2);

        assertThat(actual, isNot(sameAs(wrapped)));
        assertThat(actual, isInstanceOf(WrappedPath.class));
        assertThat(actual.wrapped(), isSameAs(wrapped));
        assertThat(actual.interceptor(), whereContentMatches(isSame(interceptor2)));
    }

    @Test
    void wrapped() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        assertThat(instance.wrapped(), sameInstance(mockPath));
    }

    @Test
    void interceptor_is_empty_by_default() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = () -> mockPath;

        assertThat(instance.interceptor(), isAbsent());
    }

    @Test
    void getFileSystem() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getFileSystem();

        verify(mockPath, times(1)).getFileSystem();
    }

    @Test
    void isAbsolute() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.isAbsolute();

        verify(mockPath, times(1)).isAbsolute();
    }

    @Test
    void getRoot() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getRoot();

        verify(mockPath, times(1)).getRoot();
    }

    @Test
    void getFileName() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getFileName();

        verify(mockPath, times(1)).getFileName();
    }

    @Test
    void getParent() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getParent();

        verify(mockPath, times(1)).getParent();
    }

    @Test
    void getNameCount() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getNameCount();

        verify(mockPath, times(1)).getNameCount();
    }

    @Test
    void getName() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getName(666);

        verify(mockPath, times(1)).getName(666);
    }

    @Test
    void subpath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.subpath(11, 66);

        verify(mockPath, times(1)).subpath(11, 66);
    }

    @Test
    void startsWith() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.startsWith("abc");

        verify(mockPath, times(1)).startsWith("abc");
    }

    @Test
    void startsWith1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.startsWith(mockPath2);

        verify(mockPath1, times(1)).startsWith(mockPath2);
    }

    @Test
    void endsWith() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.endsWith("abc");

        verify(mockPath, times(1)).endsWith("abc");
    }

    @Test
    void endsWith1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.endsWith(mockPath2);

        verify(mockPath1, times(1)).endsWith(mockPath2);
    }

    @Test
    void normalize() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.normalize();

        verify(mockPath, times(1)).normalize();
    }

    @Test
    void resolve() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.resolve("abc");

        verify(mockPath, times(1)).resolve("abc");
    }

    @Test
    void resolve1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.resolve(mockPath2);

        verify(mockPath1, times(1)).resolve(mockPath2);
    }

    @Test
    void resolveSibling() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.resolveSibling("abc");

        verify(mockPath, times(1)).resolveSibling("abc");
    }

    @Test
    void resolveSibling1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.resolveSibling(mockPath2);

        verify(mockPath1, times(1)).resolveSibling(mockPath2);
    }

    @Test
    void relativize() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.relativize(mockPath2);

        verify(mockPath1, times(1)).relativize(mockPath2);
    }

    @Test
    void toUri() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toUri();

        verify(mockPath, times(1)).toUri();
    }

    @Test
    void toAbsolutePath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toAbsolutePath();

        verify(mockPath, times(1)).toAbsolutePath();
    }

    @Test
    void toRealPath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toRealPath();

        verify(mockPath, times(1)).toRealPath();
    }

    @Test
    void toFile() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toFile();

        verify(mockPath, times(1)).toFile();
    }

    @Test
    void register() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);
        final WatchService watchService = givenWatchService();
        final Kind<?>[] kinds = givenEmptyKinds();

        instance.register(watchService, kinds);

        verify(mockPath, times(1)).register(watchService, kinds);
    }

    @Test
    void register1() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);
        final WatchService watchService = givenWatchService();
        final Modifier[] modifiers = givenEmptyModifiers();
        final Kind<?>[] kinds = givenEmptyKinds();

        instance.register(watchService, kinds, modifiers);

        verify(mockPath, times(1)).register(watchService, kinds, modifiers);
    }

    @Test
    void iterator() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.iterator();

        verify(mockPath, times(1)).iterator();
    }

    @Test
    void compareTo() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.compareTo(mockPath2);

        verify(mockPath1, times(1)).compareTo(mockPath2);
    }

    static class DefaultUnitTest {

        @Test
        void constructor_with_Interceptor() {
            final Path path = givenMockPath();
            final Interceptor interceptor = (event, result) -> ofNullable(result);

            final Default actual = new Default(path, interceptor);

            assertThat(actual.wrapped(), isSameAs(path));
            assertThat(actual.interceptor(), whereContentMatches(isSameAs(interceptor)));
        }

        @Test
        void constructor_without_Interceptor() {
            final Path path = givenMockPath();

            final Default actual = new Default(path, null);

            assertThat(actual.wrapped(), isSameAs(path));
            assertThat(actual.interceptor(), isAbsent());
        }

    }

    @SuppressWarnings({"EqualsWithItself", "EqualsBetweenInconvertibleTypes", "ResultOfMethodCallIgnored"})
    static class SupportUnitTest {

        @Test
        void equals_works_with_same_instance() {
            final Path path = givenMockPath();
            final Support instance = new Default(path, null);

            final boolean actual = instance.equals(instance);

            assertThat(actual, isEqualTo(true));
        }

        @Test
        void equals_works_with_other_wrapped() {
            final Path path = givenMockPath();
            final WrappedPath instance1 = new Default(path, null);
            final Support instance2 = new Default(path, null);

            final boolean actual = instance1.equals(instance2);

            assertThat(actual, isEqualTo(true));
        }

        @Test
        void equals_works_with_direct_argument() {
            final Path path = givenMockPath();
            final Support instance = new Default(path, null);

            final boolean actual = instance.equals(path);

            assertThat(actual, isEqualTo(true));
        }

        @Test
        void equals_works_with_everything_else() {
            final Path path = givenMockPath();
            final Support instance = new Default(path, null);

            final boolean actual = instance.equals(123);

            assertThat(actual, isEqualTo(false));
        }

        @Test
        void hashCode_works_as_expected() {
            final Path path = givenMockPath();
            final Support instance = new Default(path, null);

            final int actual = instance.hashCode();

            assertThat(actual, isEqualTo(path.hashCode()));
        }

        @Test
        void toString_works_as_expected() {
            final Path path = givenMockPath();
            doReturn("123").when(path).toString();
            final Support instance = new Default(path, null);

            final String actual = instance.toString();

            assertThat(actual, isEqualTo("123"));
        }

    }

    @Nonnull
    private static WrappedPath givenInstanceFor(@Nonnull Path wrapped) {
        return new Default(wrapped, null);
    }

    @Nonnull
    private static Path givenMockPath() {
        return mock(Path.class);
    }

    @Nonnull
    private static Modifier[] givenEmptyModifiers() {
        return new Modifier[]{};
    }

    @Nonnull
    private static Kind<?>[] givenEmptyKinds() {
        return new Kind[]{};
    }

    @Nonnull
    private static WatchService givenWatchService() {
        return mock(WatchService.class);
    }

}
